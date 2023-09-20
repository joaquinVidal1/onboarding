package com.example.proyecto_final_de_onboarding.presentation.mainscreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_final_de_onboarding.R

@Composable
fun AddButton(qty: Int, onAddUnitPressed: () -> Unit, onRemoveUnitPressed: () -> Unit, modifier: Modifier = Modifier) {
    val buttonShape = RoundedCornerShape(20.dp)
    if (qty == 0) {
        OutlinedButton(
            onClick = onAddUnitPressed,
            shape = buttonShape,
            contentPadding = PaddingValues(horizontal = 24.dp),
            border = BorderStroke(color = MaterialTheme.colors.primary, width = 1.dp),
            modifier = modifier
        ) {
            Text(text = stringResource(id = R.string.add))
        }
    } else {
        Surface(
            border = BorderStroke(color = MaterialTheme.colors.secondary, width = 1.dp),
            shape = buttonShape,
            modifier = modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemoveUnitPressed) {
                    Image(painter = painterResource(id = R.drawable.icon_minus), contentDescription = null)
                }

                Text(text = "$qty")

                IconButton(onClick = onAddUnitPressed) {
                    Image(painter = painterResource(id = R.drawable.icon_plus), contentDescription = null)
                }
            }

        }
    }
}

@Composable
@Preview
fun AddButtonPreview() {
    MaterialTheme {
        AddButton(qty = 1, onAddUnitPressed = {}, onRemoveUnitPressed = {})
    }
}