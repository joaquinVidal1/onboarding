package com.example.proyecto_final_de_onboarding.network

import androidx.annotation.DrawableRes
import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkItemContainer(val items: List<Item>)

@JsonClass(generateAdapter = true)
data class NetworkItem(
    val id: Int,
    val name: String,
    val price: Double,
    @DrawableRes val mainImage: Int,
    val kind: Kind,
    @DrawableRes val checkoutImage: Int
)

fun NetworkItemContainer.asDomainModel(): List<Item> {
    return items.map {
        Item(
            id = it.id,
            name = it.name,
            price = it.price,
            kind = it.kind,
            mainImage = it.mainImage,
            checkoutImage = it.checkoutImage
        )
    }
}


