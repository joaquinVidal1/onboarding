package com.example.proyecto_final_de_onboarding

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Kind(val header: String) {
    Fruit("Fruits"),
    Veggie("Veggies")
}
@Entity
data class Item(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val mainImage: String,
    val kind: Kind,
    val checkoutImage: String
)

fun getFromString(kind : String): Kind{
    return if ((kind=="fruit") || (kind =="Fruit") || (kind =="fruits") || (kind =="Fruits")) {
        Kind.Fruit
    }else{
        Kind.Veggie
    }

}




