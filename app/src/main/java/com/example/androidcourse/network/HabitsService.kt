package com.example.androidcourse.network

import android.util.Log
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.LOG_TAGS
import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT

private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
private const val TOKEN = "e044ba6c-0ff8-476d-9b29-85c9d2d029b0"

private val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val authInterceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val builder = originalRequest.newBuilder()
        .header("Authorization", TOKEN)
        .header("Content-Type", "application/json")
    val newRequest = builder.build()
    chain.proceed(newRequest)
}

private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

interface HabitsService {
    @GET("habit")
    suspend fun getHabits(): Response<List<Habit>>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit): Response<UUIDDto>

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: UUIDDto): Response<Unit>
}

class ApiService {
    private val service: HabitsService = retrofit.create(HabitsService::class.java)

    suspend fun getHabits(): List<Habit>? {
        return repeatTask { service.getHabits() }
    }

    suspend fun addOrUpdateHabit(habit: Habit): UUIDDto? {
        return repeatTask { service.addOrUpdateHabit(habit) }
    }

    suspend fun deleteHabit(uid: UUIDDto) {
        repeatTask { service.deleteHabit(uid) }
    }

    private suspend fun <T> repeatTask(request: suspend () -> Response<T>): T? {
        val maxTries = 5
        var tries = 0
        var isSuccess = false
        var result: T? = null
        while (!isSuccess && tries < maxTries) {
            try {
                val response = request()
                if (response.isSuccessful) {
                    result = response.body()!!
                } else {
                    Log.e(LOG_TAGS.NETWORK, response.message())
                    break
                }
                Log.d(LOG_TAGS.NETWORK, "Задача выполнена, попыток: $tries")
                isSuccess = true
            } catch (e: Exception) {
                Log.d(LOG_TAGS.NETWORK, "Задача не выполнена, попытка: $tries", e)
                tries++
                delay(2000)
            }
        }
        if (!isSuccess) {
            Log.e(LOG_TAGS.NETWORK, "Задача не выполнена, попыток: $tries")
        }
        return result
    }
}

val service = ApiService()