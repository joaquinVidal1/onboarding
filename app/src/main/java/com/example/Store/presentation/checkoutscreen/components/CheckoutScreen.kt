package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

@Composable
fun CheckoutScreen(
    cart: List<ScreenListItem.ScreenItem>,
    totalAmount: String,
    onBackPressed: () -> Unit,
    onCheckoutPressed: () -> Unit
) {
    val listState = rememberLazyGridState()

    Column(modifier = Modifier.padding(horizontal = 12.dp)) {

        Spacer(modifier = Modifier.size(12.dp))

        IconButton(
            onClick = onBackPressed, modifier = Modifier.padding(start = 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(text = stringResource(id = R.string.shopping_cart_text))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            state = listState,
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            items(items = cart, key = { it.product.id }) {
                CartItem(
                    item = it, modifier = Modifier
                        .padding(
                            start = 6.dp, end = 6.dp
                        )
                        .width(150.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.totalText))

            Text(text = stringResource(id = R.string.price, totalAmount))

        }

        Spacer(modifier = Modifier.size(30.dp))

        FloatingActionButton(
            onClick = onCheckoutPressed,
            shape = RoundedCornerShape(50),
            backgroundColor = colorResource(
                id = R.color.color_checkout_button
            ),
            modifier = Modifier.align(CenterHorizontally).fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.checkout),
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(
                    vertical = 14.dp,
                ),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.size(40.dp))

    }
}

@Composable
@Preview
fun CheckoutScreenPreview() {
    MaterialTheme {
        Surface {
            CheckoutScreen(cart = listOf(
                ScreenListItem.ScreenItem(
                    product = Product(
                        id = 1,
                        name = "Kiwi",
                        price = 5.0,
                        mainImage = "",
                        checkoutImage = "",
                        kind = Kind.Fruit
                    ), quantity = 1
                )
            ),
                onBackPressed = { },
                totalAmount = "0.0",
                onCheckoutPressed = { })
        }
    }
}