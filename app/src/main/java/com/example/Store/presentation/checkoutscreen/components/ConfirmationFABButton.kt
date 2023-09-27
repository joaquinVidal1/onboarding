package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_final_de_onboarding.R


private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

@Composable
fun ConfirmationFABButton(
    text: String,
    isEnabled: Boolean,
    onButtonPressed: () -> Unit,
    enabledColor: Color,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalRippleTheme provides if (isEnabled) LocalRippleTheme.current else NoRippleTheme
    ) {

        FloatingActionButton(
            onClick = { if (isEnabled) onButtonPressed() },
            shape = RoundedCornerShape(50),
            backgroundColor = if (isEnabled) enabledColor
            else Color.Gray,
            modifier = modifier
        ) {
            Text(
                text = text,
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(
                    vertical = 14.dp,
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
    }
}