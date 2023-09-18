package com.example.proyecto_final_de_onboarding.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartTable")
data class CartItem(
    @PrimaryKey
    val productId: Int,
    var cant: Int)