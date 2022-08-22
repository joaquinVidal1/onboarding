package com.example.proyecto_final_de_onboarding.data

import com.example.proyecto_final_de_onboarding.CartItem

object CartRepository {

    var cart = mutableListOf<CartItem>()
        fun addItem(id: Int): List<CartItem>{
            var found = false
            cart = cart.map {
                if (it.itemId == id) {
                    found = true
                    it.increment()
                } else it
            } as MutableList<CartItem>
            if (!found) {
                cart = cart.apply {
                    add(CartItem(id, 1))
                }
            }
            return cart

        }
        fun removeItem(id: Int): List<CartItem>{
            val itemToRemove = cart.find { it.itemId == id }
            if (itemToRemove?.cant == 1){
                cart.remove(itemToRemove)
            }else{
                itemToRemove?.cant = itemToRemove?.cant!!?.minus(1)
            }
            return cart
        }

    fun editQuantity(id: Int, qant: Int): List<CartItem>{
        val itemToEdit = cart.find { it.itemId == id }
        if (qant == 0){
            cart.remove(itemToEdit)
        }else {
            itemToEdit?.cant = qant
        }
        return cart
    }

    fun cleanCart(): MutableList<CartItem> {
        cart = mutableListOf()
        return cart
    }

}