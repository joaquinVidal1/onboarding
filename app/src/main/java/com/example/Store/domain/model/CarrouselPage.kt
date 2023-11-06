package com.example.Store.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CarrouselPage(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    @DrawableRes val image: Int
)