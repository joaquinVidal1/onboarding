package com.example.proyecto_final_de_onboarding

import androidx.annotation.DrawableRes

enum class Kind(val header: String) {
    Fruit("Fruits"),
    Veggie("Veggies")
}


data class Item(
    val id: Int,
    val name: String,
    val price: Int,
    @DrawableRes val mainImage: Int,
    val kind: Kind,
    @DrawableRes val checkoutImage: Int? = null
)



