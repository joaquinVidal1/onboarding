package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.database.CartDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val cartDatabase: CartDatabase) {
//    private val cartList: LiveData<List<CartItem>> =
//        Transformations.map(cartDatabase.cartDao.getCartItems()) {
//            it
//        }

    //  private val itemsRepository = ItemsRepository()

    private val _cart =
        Transformations.map(cartDatabase.cartDao.getCartItems()) {
            it ?: listOf()
        }
    val cart: LiveData<List<CartItem>>
        get() = _cart

    suspend fun addItem(id: Int) {
        val updatedCart: List<CartItem>
        val itemToAdd = _cart.value?.find { it.itemId == id }
        if (itemToAdd != null) {
            //           itemToAdd.cant.plus(1)
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
            withContext(Dispatchers.IO) {
                cartDatabase.cartDao.removeFromCartDB(itemToRemove.itemId)
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
            withContext(Dispatchers.IO) {
                cartDatabase.cartDao.removeFromCartDB(itemToEdit!!.itemId)
            }
        } else {
            updatedCart = _cart.value.apply {
                _cart.value!!.find { it.itemId == id }!!.cant = qty
            } ?: listOf()
            updateCart(updatedCart)
        }
    }

    suspend fun cleanCart() {
        withContext(Dispatchers.IO) {
            cartDatabase.cartDao.emptyTable()
        }
    }

    fun getQty(itemId: Int) =
        cart.value?.find { it.itemId == itemId }?.cant ?: 0

    private suspend fun updateCart(cart: List<CartItem>) {
        withContext(Dispatchers.IO) {
            cartDatabase.cartDao.insertAll(cart)
        }
    }

}

private lateinit var INSTANCE: CartRepository

fun getCartRepository(CartDatabase: CartDatabase): CartRepository {
    if (!::INSTANCE.isInitialized) {
        INSTANCE = CartRepository(CartDatabase)
    }
    return INSTANCE
}