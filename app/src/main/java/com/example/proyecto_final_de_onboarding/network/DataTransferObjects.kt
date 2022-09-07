package com.example.proyecto_final_de_onboarding.network

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.getFromString
import com.squareup.moshi.JsonClass

//se deberia usar si en el json llega un objeto con la lista, pero llega una lista
//@JsonClass(generateAdapter = true)
//data class NetworkItemContainer(val items: List<NetworkItem>)

@JsonClass(generateAdapter = true)
data class NetworkItem(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val checkoutImageUrl: String,
    val listImageUrl: String
)

fun NetworkItem.asDomainModel(): Item {
    return Item(
        id = id,
        name = name,
        price = price,
        kind = getFromString(category),
        mainImage = listImageUrl,
        checkoutImage = checkoutImageUrl
    )
}

