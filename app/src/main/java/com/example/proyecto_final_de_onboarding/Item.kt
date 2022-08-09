package com.example.proyecto_final_de_onboarding

import androidx.annotation.DrawableRes

enum class Kind{
    fruit, veggie
}

class Item(val id: Int, val name: String, val price: Int, @DrawableRes val image: Int, val kind: Kind) {
}

data class CartItem(val itemId: Int, var cant: Int){
    fun increment(): CartItem{
        return CartItem(
            itemId,
            this.cant + 1
        )
    }
    fun decrement(): CartItem{
        return CartItem(itemId, if (this.cant>0) this.cant-1 else this.cant)
    }
}

data class ScreenItem(val item: Item, val cant: Int){}

