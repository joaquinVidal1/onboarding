package com.example.proyecto_final_de_onboarding.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.proyecto_final_de_onboarding.CartItem

object CartRepository {
    private val _cart: MutableLiveData<List<CartItem>> = MutableLiveData<List<CartItem>>(listOf())
    val cart: LiveData<List<CartItem>>
        get() = _cart

    fun addItem(id: Int) {
        val itemToAdd = _cart.value?.find { it.itemId == id }
        if (itemToAdd != null) {
            itemToAdd.cant.plus(1)
//            _cart.value =
//                _cart.value!!.toMutableList().apply {
//                    find { it.itemId == id }
//                        ?.cant?.plus(1)
//                }
        } else {
            _cart.value = _cart.value?.toMutableList()?.apply { add(CartItem(id, 1)) }
        }
    }

    fun removeItem(id: Int) {
        val itemToRemove = _cart.value?.find { it.itemId == id }
        if (itemToRemove?.cant == 1) {
            _cart.value = cart.value?.toMutableList()?.apply { remove(itemToRemove)}
        } else {
            itemToRemove?.cant = itemToRemove?.cant!!.minus(1)
        }
    }

    fun editQuantity(id: Int, qty: Int) {
        val itemToEdit = _cart.value!!.find { it.itemId == id }
        if (qty == 0) {
            _cart.value = cart.value?.toMutableList()?.apply { remove(itemToEdit)}
        } else {
            itemToEdit?.cant = qty
        }
    }

    fun cleanCart() {
        _cart.value = mutableListOf()
    }

}