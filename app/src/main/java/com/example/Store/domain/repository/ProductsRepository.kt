package com.example.proyecto_final_de_onboarding.domain.repository

import com.example.proyecto_final_de_onboarding.domain.model.Product

interface ProductsRepository {
    suspend fun getProducts(): List<Product>

    suspend fun refreshProducts()

    suspend fun getProduct(productId: Int): Product
}