package com.example.proyecto_final_de_onboarding.domain.repository

import com.example.proyecto_final_de_onboarding.domain.model.CartItem

interface CartRepository {
    suspend fun addProduct(productId: Int): List<CartItem>

    suspend fun removeProduct(productId: Int): List<CartItem>

    suspend fun getCart(): List<CartItem>
}