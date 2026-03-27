package ru.itis.hw6.data.network

import com.google.gson.internal.GsonBuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.itis.hw6.BuildConfig
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val BASE_URL = BuildConfig.GENIUS_API_URL
    private val token = BuildConfig.GENIUS_API_KEY

    private val client = OkHttpClient.Builder()
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(AuthorizationInterceptor(token))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val geniusApi = retrofit.create(GeniusApi::class.java)

    fun getGeniusApi() = geniusApi

}