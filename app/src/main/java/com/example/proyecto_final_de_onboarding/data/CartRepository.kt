package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.database.ItemsDatabase
import com.example.proyecto_final_de_onboarding.database.asCartModel

class CartRepository(database: ItemsDatabase) {
    private val cartList: LiveData<List<CartItem>> =
        Transformations.map(database.itemDao.getItems()) {
            it?.asCartModel() ?: listOf()
        }

    //  private val itemsRepository = ItemsRepository()

    private val _cart = MutableLiveData<List<CartItem>>(cartList.value ?: listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart

    fun addItem(id: Int) {
        val itemToAdd = _cart.value?.find { it.itemId == id }
        if (itemToAdd != null) {
            //           itemToAdd.cant.plus(1)
            _cart.value =
                _cart.value!!.toMutableList().apply {
                    find { it.itemId == id }
                        ?.apply { cant = cant.plus(1) }

                }
        } else {
            _cart.value = _cart.value?.toMutableList()?.apply { add(CartItem(id, 1)) }
        }
    }

    fun removeItem(id: Int) {
        val itemToRemove = _cart.value?.find { it.itemId == id }
        if (itemToRemove?.cant == 1) {
            _cart.value = _cart.value?.toMutableList()?.apply { remove(itemToRemove) }
        } else {
            _cart.value =
                _cart.value!!.toMutableList().apply {
                    find { it.itemId == id }
                        ?.apply { cant = cant.minus(1) }
                }
        }
    }

    fun editQuantity(id: Int, qty: Int) {
        val itemToEdit = _cart.value!!.find { it.itemId == id }
        if (qty == 0) {
            _cart.value = _cart.value?.toMutableList()?.apply { remove(itemToEdit) }
        } else {
            _cart.value = _cart.value.apply {
                _cart.value!!.find { it.itemId == id }!!.cant = qty
            }
        }
    }

    fun cleanCart() {
        _cart.value = mutableListOf()
    }

    fun getQty(itemId: Int) =
        cart.value?.find { it.itemId == itemId }?.cant ?: 0
}

private lateinit var INSTANCE: CartRepository
fun getCartRepository(database: ItemsDatabase): CartRepository {
    if (!::INSTANCE.isInitialized) {
        INSTANCE = CartRepository(database)
    }
    return INSTANCE
}