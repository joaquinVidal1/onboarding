package com.example.proyecto_final_de_onboarding.domain.repository

import com.example.proyecto_final_de_onboarding.domain.model.Product

interface ProductsRepository {

    abstract suspend fun getProducts(): List<Product>
}