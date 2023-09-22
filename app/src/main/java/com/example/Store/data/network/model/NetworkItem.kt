package com.example.proyecto_final_de_onboarding.data.network.model

import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.getFromString

data class NetworkItem(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val checkoutImageUrl: String,
    val listImageUrl: String
) {

    fun asDomainModel(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            kind = getFromString(category),
            mainImage = listImageUrl,
            checkoutImage = checkoutImageUrl
        )
    }

}