package com.example.androidcourse.network

import com.example.androidcourse.core.Habit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://droid-test-server.doubletapp.ru/api/"
private const val TOKEN = "e044ba6c-0ff8-476d-9b29-85c9d2d029b0"

private val authInterceptor = Interceptor { chain ->
    val originalRequest = chain.request()
    val builder = originalRequest.newBuilder().header(
        "Authorization",
        TOKEN
    )
    val newRequest = builder.build()
    chain.proceed(newRequest)
}

private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(authInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()


interface HabitsService {
    @GET("habit")
    suspend fun getHabits(): List<Habit>
}

val service: HabitsService = retrofit.create(HabitsService::class.java)