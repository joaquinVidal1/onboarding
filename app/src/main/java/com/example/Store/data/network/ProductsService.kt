package com.example.proyecto_final_de_onboarding.data.network

import com.example.proyecto_final_de_onboarding.data.network.model.NetworkItem
import retrofit2.http.GET

interface ProductsService {
    @GET("products")
    suspend fun getProducts(): List<NetworkItem>
}