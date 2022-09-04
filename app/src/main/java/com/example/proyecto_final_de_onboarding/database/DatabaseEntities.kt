package com.example.proyecto_final_de_onboarding.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind

//entidad que se guarda en la base de datos
@Entity
data class DatabaseItem constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val kind: Kind,
    val mainImage: Int,
    val checkoutImage: Int,
    var qty: Int =0
)

//convierte de lo que saca de la base de datos a lo que usa la app
fun List<DatabaseItem>.asDomainModel(): List<Item> {
    return map {
        Item(
            id = it.id,
            name = it.name,
            kind = it.kind,
            price = it.price,
            mainImage = it.mainImage,
            checkoutImage = it.checkoutImage
        )
    }
}

//convierte de lo que saca de la base de datos en un cartItem
fun List<DatabaseItem>.asCartModel(): List<CartItem> {
    return map {
        CartItem(
            itemId = it.id,
            cant = it.qty
        )
    }
}

