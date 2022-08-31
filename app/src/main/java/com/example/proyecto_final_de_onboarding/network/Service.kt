package com.example.proyecto_final_de_onboarding.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ItemService {
    @GET("items")
    suspend fun getItems(): NetworkItemContainer
}

object ItemNetwork{
    private val retrofit = Retrofit.Builder()
        .baseUrl("mobile-api.inhouse.decemberlabs.com")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val items = retrofit.create(ItemService::class.java)
}