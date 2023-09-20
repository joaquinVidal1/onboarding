package com.example.proyecto_final_de_onboarding.domain

import androidx.annotation.DrawableRes

data class CarrouselPage(
    val title: String,
    val subtitle: String,
    @DrawableRes val image: Int
)