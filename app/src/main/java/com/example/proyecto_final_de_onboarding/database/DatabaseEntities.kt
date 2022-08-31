package com.example.proyecto_final_de_onboarding.database

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyecto_final_de_onboarding.CartItem
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind

//entidad que se guarda en la base de datos
@Entity
data class DataBaseItem constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val price: Double,
    val kind: Kind,
    @DrawableRes val mainImage: Int,
    @DrawableRes val checkoutImage: Int,
    val cant: Int
)

//convierte de lo que saca de la base de datos a lo que usa la app
fun List<DataBaseItem>.asDomainModel(): List<Item> {
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
fun List<DataBaseItem>.asCarritoModel(): List<CartItem> {
    return map {
        CartItem(
            itemId = it.id,
            cant = it.cant
        )
    }
}

