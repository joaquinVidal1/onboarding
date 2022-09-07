package com.example.proyecto_final_de_onboarding.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartTable")
data class CartItem(
    @PrimaryKey
    val itemId: Int,
    var cant: Int)