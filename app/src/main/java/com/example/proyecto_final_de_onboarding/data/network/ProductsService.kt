package com.example.proyecto_final_de_onboarding.data.network

import retrofit2.http.GET
import retrofit2.http.Header

interface ProductsService {
    @GET("/products")
    suspend fun getItems(
        @Header("AUTHORIZATION") auth: String = "Bearer 0a41c523-fa00-418a-a585-7dd1fc5f18e4"
    ): List<>

}