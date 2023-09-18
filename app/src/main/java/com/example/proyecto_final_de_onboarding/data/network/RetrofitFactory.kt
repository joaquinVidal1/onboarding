package com.example.proyecto_final_de_onboarding.data.network

import com.example.proyecto_final_de_onboarding.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {

    fun getBuilder(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            // Request
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("AUTHORIZATION", "Bearer $API_TOKEN")
            val response = chain.proceed(requestBuilder.build())
            response
        })

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL)
            .client(client.build()).build()
    }
}

const val BASE_URL = "https://api.ecommerce.inhouse.decemberlabs.com/"
const val API_TOKEN = "0a41c523-fa00-418a-a585-7dd1fc5f18e4"