package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.database.CartDao
import com.example.proyecto_final_de_onboarding.domain.entities.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(private val cartDao: CartDao) {

    private val _cart =
        Transformations.map(cartDao.getCartItems()) {
            it ?: listOf()
        }
    val cart: LiveData<List<CartItem>>
        get() = _cart

    suspend fun addItem(id: Int) {
        val updatedCart: List<CartItem>
        // TODO podrías concatenar _cart.value?.find { it.itemId == id }?.let{ ... }, hilando fino va a mejorar la sintaxis cuando estés más familiarizado con Kotlin en general
        val itemToAdd = _cart.value?.find { it.itemId == id }
        if (itemToAdd != null) {
            updatedCart =
                _cart.value!!.toMutableList().apply {
                    find { it.itemId == id }
                        ?.apply { cant = cant.plus(1) }
                }
        } else {
            updatedCart = _cart.value?.toMutableList()?.apply { add(CartItem(id, 1)) } ?: listOf()
        }
        updateCart(updatedCart)
    }

    suspend fun removeItem(id: Int) {
        val updatedCart: List<CartItem>
        val itemToRemove = _cart.value?.find { it.itemId == id }
        if (itemToRemove?.cant == 1) {
            // TODO no es necesario hacer el withContext, revisá a ver si te das cuenta porqué
            withContext(Dispatchers.IO) {
                cartDao.removeFromCartDB(itemToRemove.itemId)
            }
        } else {
            updatedCart =
                _cart.value!!.toMutableList().apply {
                    find { it.itemId == id }
                        ?.apply { cant = cant.minus(1) }
                }
            updateCart(updatedCart)
        }
    }

    suspend fun editQuantity(id: Int, qty: Int) {
        val updatedCart: List<CartItem>
        val itemToEdit = _cart.value!!.find { it.itemId == id }
        if (qty == 0) {
            // TODO no es necesario hacer el withContext, revisá a ver si te das cuenta porqué
            withContext(Dispatchers.IO) {
                cartDao.removeFromCartDB(itemToEdit!!.itemId)
            }
        } else {
            updatedCart = _cart.value.apply {
                _cart.value!!.find { it.itemId == id }!!.cant = qty
            } ?: listOf()
            updateCart(updatedCart)
        }
    }

    suspend fun cleanCart() {
        // TODO no es necesario hacer el withContext, revisá a ver si te das cuenta porqué
        withContext(Dispatchers.IO) {
            cartDao.emptyTable()
        }
    }

    private suspend fun updateCart(cart: List<CartItem>) {
        // TODO no es necesario hacer el withContext, revisá a ver si te das cuenta porqué
        withContext(Dispatchers.IO) {
            cartDao.insertAll(cart)
        }
    }

}
