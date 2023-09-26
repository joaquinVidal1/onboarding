package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onCheckoutPressed: () -> Unit,
    isCheckoutButtonEnabled: Boolean,
    onItemPressed: (ScreenListItem.ScreenItem) -> Unit,
) {
    val listState = rememberLazyGridState()

    Column {
        Spacer(modifier = Modifier.size(12.dp))

        IconButton(
            onClick = onBackPressed
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))


        Column(modifier = Modifier.padding(horizontal = 12.dp)) {

            Text(text = stringResource(id = R.string.shopping_cart_text))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 150.dp),
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(vertical = 24.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(items = cart, key = { item -> item.product.id }) { item ->
                    CartItem(item = item,
                        modifier = Modifier
                            .width(150.dp)
                            .clickable { onItemPressed(item) })
                }
            }

            Column(modifier = Modifier.weight(0.3f)) {


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.totalText))

                    Text(
                        text = stringResource(
                            id = R.string.price, totalAmount
                        )
                    )
                }

                Spacer(modifier = Modifier.size(30.dp))

                CompositionLocalProvider(
                    LocalRippleTheme provides if (isCheckoutButtonEnabled) LocalRippleTheme.current else NoRippleTheme
                ) {

                    FloatingActionButton(
                        onClick = { if (isCheckoutButtonEnabled) onCheckoutPressed() },
                        shape = RoundedCornerShape(50),
                        backgroundColor = colorResource(
                            id = if (isCheckoutButtonEnabled) R.color.color_checkout_button
                            else R.color.grey
                        ),
                        modifier = Modifier
                            .align(CenterHorizontally)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.checkout),
                            color = colorResource(id = R.color.white),
                            modifier = Modifier.padding(
                                vertical = 14.dp,
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.size(40.dp))

            }
        }
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
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
                ), ScreenListItem.ScreenItem(
                    product = Product(
                        id = 1,
                        name = "Kiwi",
                        price = 5.0,
                        mainImage = "",
                        checkoutImage = "",
                        kind = Kind.Fruit
                    ), quantity = 1
                ), ScreenListItem.ScreenItem(
                    product = Product(
                        id = 1,
                        name = "Kiwi",
                        price = 5.0,
                        mainImage = "",
                        checkoutImage = "",
                        kind = Kind.Fruit
                    ), quantity = 1
                ), ScreenListItem.ScreenItem(
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
                onCheckoutPressed = { },
                isCheckoutButtonEnabled = true,
                onItemPressed = { _ -> })
        }
    }
}