package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

@Composable
fun CartItem(item: ScreenListItem.ScreenItem, modifier: Modifier = Modifier) {
    Surface {

        Column(modifier = modifier) {

            AsyncImage(
                model = item.product.checkoutImage,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(19.dp))

            val paddingFromImage = 2.dp

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingFromImage)
            ) {
                Text(
                    text = item.product.name,
                )

                Text(
                    text = stringResource(
                        id = R.string.price, item.product.roundedPrice
                    )
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = pluralStringResource(
                    id = R.plurals.unit, count = item.quantity
                ), modifier = Modifier.padding(start = paddingFromImage)
            )
        }
    }
}

@Composable
@Preview
fun CartItemPreview() {
    MaterialTheme {
        CartItem(
            item = ScreenListItem.ScreenItem(
                product = Product(
                    id = 1,
                    name = "Kiwi",
                    price = 5.0,
                    mainImage = "",
                    checkoutImage = "",
                    kind = Kind.Fruit
                ), quantity = 1
            )
        )
    }
}