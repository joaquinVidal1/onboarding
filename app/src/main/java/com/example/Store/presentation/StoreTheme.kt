package com.example.Store.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.proyecto_final_de_onboarding.R


@Immutable
data class ExtendedColors(
    val primary: Color, val secondary: Color
)

@Composable
fun StoreTheme(
    content: @Composable () -> Unit
) {
    val h1 = TextStyle(
        color = colorResource(id = R.color.item_name_color),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val h2 = TextStyle(
        color = colorResource(id = R.color.item_price_color),
        fontSize = 16.sp,
        lineHeight = 20.sp
    )

    val storeTypography = Typography(
        titleMedium = h1, titleSmall = h2
    )

    MaterialTheme(typography = storeTypography) {
        content
    }
}