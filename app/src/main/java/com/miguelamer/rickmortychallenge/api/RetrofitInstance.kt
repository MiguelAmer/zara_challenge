package com.miguelamer.rickmortychallenge.api

import com.miguelamer.rickmortychallenge.Constants.Companion.BASE_URL
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).connectionPool(
                ConnectionPool(0, 5, TimeUnit.MINUTES)
            ).build()
            Retrofit.Builder().baseUrl(BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(client).build()
        }

        val api: CharactersAPI by lazy {
            retrofit.create(CharactersAPI::class.java)
        }
    }
}