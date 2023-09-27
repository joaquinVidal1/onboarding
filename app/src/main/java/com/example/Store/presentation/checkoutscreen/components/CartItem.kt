package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.Store.presentation.StoreTheme
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

@Composable
fun CartItem(item: ScreenListItem.ScreenItem, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {

        AsyncImage(
            model = item.product.checkoutImage,
            contentDescription = null,
            placeholder = painterResource(id = R.drawable.placeholder),
            error = painterResource(id = R.drawable.placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp)),
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
                style = MaterialTheme.typography.h1
            )

            Text(
                text = stringResource(
                    id = R.string.price, item.product.roundedPrice
                ), style = MaterialTheme.typography.h2
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = pluralStringResource(
                id = R.plurals.unit, item.quantity, item.quantity
            ),
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(start = paddingFromImage)
        )
    }

}

@Composable
@Preview
fun CartItemPreview() {
    Surface {
        StoreTheme {
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
}