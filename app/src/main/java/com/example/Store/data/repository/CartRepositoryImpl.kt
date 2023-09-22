package com.example.proyecto_final_de_onboarding.data.repository

import com.example.proyecto_final_de_onboarding.data.db.CartDao
import com.example.proyecto_final_de_onboarding.domain.model.CartItem
import com.example.proyecto_final_de_onboarding.domain.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(private val cartDao: CartDao) : CartRepository {

    override suspend fun emptyCart() {
        withContext(Dispatchers.IO) {
            cartDao.emptyTable()
        }
    }

    override suspend fun editProductQuantity(productId: Int, qty: Int): List<CartItem> {
        return cartDao.editProductQuantity(CartItem(productId = productId, quantity = qty))
    }

    private suspend fun updateCart(cart: List<CartItem>) {
        withContext(Dispatchers.IO) {
            cartDao.insertAll(cart)
        }
    }

    override suspend fun addProduct(productId: Int): List<CartItem> {
        return withContext(Dispatchers.IO) {
            cartDao.increaseProductQuantity(productId)
        }
    }


    override suspend fun removeProduct(productId: Int): List<CartItem> {
        return withContext(Dispatchers.IO) {
            cartDao.decreaseProductQuantity(productId)
        }
    }

    override suspend fun getCart(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            cartDao.getCartItems()
        }
    }

}
