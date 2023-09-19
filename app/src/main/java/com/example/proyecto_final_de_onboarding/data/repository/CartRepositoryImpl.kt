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

//    suspend fun editQuantity(id: Int, qty: Int) {
//        val updatedCart: List<CartItem>
//        val itemToEdit = _cart.value!!.find { it.itemId == id }
//        if (qty == 0) {
//            withContext(Dispatchers.IO) {
//                cartDao.removeFromCartDB(itemToEdit!!.itemId)
//            }
//        } else {
//            updatedCart = _cart.value.apply {
//                _cart.value!!.find { it.itemId == id }!!.cant = qty
//            } ?: listOf()
//            updateCart(updatedCart)
//        }
//    }

    override suspend fun emptyCart() {
        withContext(Dispatchers.IO) {
            cartDao.emptyTable()
        }
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
