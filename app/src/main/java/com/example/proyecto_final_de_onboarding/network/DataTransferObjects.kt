package com.example.proyecto_final_de_onboarding.network

import com.example.proyecto_final_de_onboarding.Item
import com.example.proyecto_final_de_onboarding.Kind
import com.example.proyecto_final_de_onboarding.database.DatabaseItem
import com.example.proyecto_final_de_onboarding.getFromString
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkItemContainer(val items: List<NetworkItem>)

@JsonClass(generateAdapter = true)
data class NetworkItem(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val checkoutImageUrl: String,
    val listImageUrl: String
)

    fun NetworkItem.asDatabaseModel(): DatabaseItem {
        return DatabaseItem(
            id = id,
            name = name,
            price = price,
            kind = getFromString(category),
            mainImage = listImageUrl,
            checkoutImage = checkoutImageUrl
        )
    }


fun NetworkItemContainer.asDomainModel(): List<Item> {
    return items.map {
        Item(
            id = it.id,
            name = it.name,
            price = it.price,
            kind = Kind.valueOf(it.category.subSequence(0,1).toString().uppercase() + it.category.subSequence(1,it.category.length).toString() ),
            mainImage = it.listImageUrl,
            checkoutImage = it.checkoutImageUrl
        )
    }
}


fun NetworkItemContainer.asDatabaseModel(): List<DatabaseItem>{
    return items.map {
        DatabaseItem(
            id = it.id,
            name = it.name,
            price = it.price,
            kind = Kind.valueOf(it.category.subSequence(0,1).toString().uppercase() + it.category.subSequence(1,it.category.length).toString()),
            mainImage = it.listImageUrl,
            checkoutImage = it.checkoutImageUrl)
    }
}


