package com.example.proyecto_final_de_onboarding.domain.repository

import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addProduct(productId: Int): List<CartItem>

    suspend fun removeProduct(productId: Int): List<CartItem>

    fun getCart(): Flow<List<CartItem>>

    suspend fun emptyCart()

    suspend fun editProductQuantity(productId: Int, qty: Int): List<CartItem>
}