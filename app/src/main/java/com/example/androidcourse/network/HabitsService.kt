package com.example.androidcourse.network

import com.example.androidcourse.core.Habit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    suspend fun getHabits(): List<Habit>

    @PUT("habit")
    suspend fun addOrUpdateHabit(@Body habit: Habit): UUIDDto

    @HTTP(method = "DELETE", path = "habit", hasBody = true)
    suspend fun deleteHabit(@Body uid: UUIDDto)
}

val service: HabitsService = retrofit.create(HabitsService::class.java)