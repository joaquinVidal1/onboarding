package com.example.proyecto_final_de_onboarding.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.RoundingMode
import java.text.DecimalFormat

enum class Kind(val header: String) {
    Fruit("Fruits"), Veggie("Veggies")
}

@Entity(tableName = "productsTable")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val mainImage: String,
    val kind: Kind,
    val checkoutImage: String
) {
    fun matchesQuery(query: String?) =
        query.isNullOrEmpty() || this.kind.name.lowercase().contains(query) || this.name.lowercase().contains(query)
}

fun getFromString(kind: String): Kind {
    return if ((kind == "fruit") || (kind == "Fruit") || (kind == "fruits") || (kind == "Fruits")) {
        Kind.Fruit
    } else {
        Kind.Veggie
    }

}

fun getRoundedPrice(price: Double): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    df.minimumFractionDigits = 2
    return df.format(price)
}




