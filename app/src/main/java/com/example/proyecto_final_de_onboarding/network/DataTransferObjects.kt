package com.example.proyecto_final_de_onboarding.network

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.database.DatabaseItem
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkItemContainer(val items: List<NetworkItem>)

@JsonClass(generateAdapter = true)
data class NetworkItem(
    val id: Int,
    val name: String,
    val price: Double,
    val mainImage: Int?,
    val kind: Kind,
    val checkoutImage: Int?
)

fun NetworkItemContainer.asDomainModel(): List<Item> {
    return items.map {
        Item(
            id = it.id,
            name = it.name,
            price = it.price,
            kind = it.kind,
            mainImage = it.mainImage?: 0,
            checkoutImage = it.checkoutImage
        )
    }
}


fun NetworkItemContainer.asDatabaseModel(): List<DatabaseItem>{
    return items.map {
        DatabaseItem(
            id = it.id,
            name = it.name,
            price = it.price,
            kind = it.kind,
            mainImage = it.mainImage ?: 0,
            checkoutImage = it.checkoutImage ?: 0)
    }
}


