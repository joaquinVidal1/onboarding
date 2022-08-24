package com.example.proyecto_final_de_onboarding.data

import com.example.proyecto_final_de_onboarding.CartItem

object CartRepository {

    var cart = mutableListOf<CartItem>()

    fun addItem(id: Int): List<CartItem> {
        val itemToAdd = cart.find { it.itemId == id }
        if (itemToAdd != null) {
            cart.find { it.itemId == id }!!.cant++
        } else {
            cart.add(CartItem(id, 1))
        }
        return cart.toList()
    }

    fun removeItem(id: Int): List<CartItem> {
        val itemToRemove = cart.find { it.itemId == id }
        if (itemToRemove?.cant == 1) {
            cart.remove(itemToRemove)
        } else {
            itemToRemove?.cant = itemToRemove?.cant!!.minus(1)
        }
        return cart.toList()
    }

    fun editQuantity(id: Int, qant: Int): List<CartItem> {
        val itemToEdit = cart.find { it.itemId == id }
        if (qant == 0) {
            cart.remove(itemToEdit)
        } else {
            itemToEdit?.cant = qant
        }
        return cart
    }

    fun cleanCart(): MutableList<CartItem> {
        cart = mutableListOf()
        return cart
    }

}