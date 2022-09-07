package com.example.proyecto_final_de_onboarding.network

import com.example.proyecto_final_de_onboarding.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface ItemService {
    @GET("/products")
    suspend fun getItems(
        @Header("AUTHORIZATION") auth: String = "Bearer 0a41c523-fa00-418a-a585-7dd1fc5f18e4"
    ): List<NetworkItem>
}

object ItemNetwork {

    val items = getBuilder().create(ItemService::class.java)

    private fun getBuilder(): Retrofit {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://mobile-api.inhouse.decemberlabs.com")
            .client(client.build())
            .build()
    }
}