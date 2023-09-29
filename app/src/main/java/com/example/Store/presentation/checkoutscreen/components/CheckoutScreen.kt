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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.Store.presentation.checkoutscreen.CheckoutScreenViewModel
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

@Composable
fun CheckoutScreen(
    onBackPressed: () -> Unit,
    onCheckoutPressed: () -> Unit,
) {
    val listState = rememberLazyGridState()
    val viewModel: CheckoutScreenViewModel = hiltViewModel()

    val cart by viewModel.screenList.collectAsState(
        initial = listOf()
    )
    val totalAmount by viewModel.totalAmount.collectAsState(
        initial = "0.0"
    )
    val isCheckoutButtonEnabled: Boolean by viewModel.showCheckoutButton.collectAsState(
        initial = false
    )


    var showBottomSheet by remember {
        mutableStateOf(
            Pair<Boolean, ScreenListItem.ScreenItem?>(
                false, null
            )
        )
    }

    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Spacer(modifier = Modifier.size(12.dp))

        IconButton(
            onClick = onBackPressed
        ) {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.layout { measurable, constraints ->
                    val padding = 12.dp
                    val noPaddingConstraints = constraints.copy(
                        maxWidth = constraints.maxWidth + (padding * 2).roundToPx()
                    )
                    val placeable = measurable.measure(noPaddingConstraints)
                    layout(placeable.width, placeable.height) {
                        placeable.place(x = -padding.roundToPx(), y = 0)
                    }
                })
        }

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = stringResource(id = R.string.shopping_cart_text),
            style = MaterialTheme.typography.h1,
            fontSize = 22.sp
        )

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
                        .clickable { showBottomSheet = Pair(true, item) })
            }
        }

        Column(modifier = Modifier.weight(0.3f)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.totalText),
                    style = MaterialTheme.typography.h1,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = stringResource(
                        id = R.string.price, totalAmount
                    ), style = MaterialTheme.typography.h1, fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.size(30.dp))

            ConfirmationFABButton(
                text = stringResource(id = R.string.checkout),
                isEnabled = isCheckoutButtonEnabled,
                onButtonPressed = {
                    viewModel.cleanCart()
                    onCheckoutPressed()
                },
                enabledColor = colorResource(
                    id = R.color.color_checkout_button
                ),
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(40.dp))

        }
    }

    if (showBottomSheet.first) {
        showBottomSheet.second?.let {
            ModifyProductQuantityDialog(
                productQty = it.quantity,
                product = it.product,
                onDisMissDialog = { showBottomSheet = Pair(false, null) },
                onEditQuantityConfirmed = {
                    viewModel.itemQtyChanged(
                        productId = it.product.id, newQty = it.quantity
                    )
                    showBottomSheet = Pair(false, null)
                },
                onRemoveUnitPressed = {
                    showBottomSheet = showBottomSheet.copy(
                        second = it.copy(
                            quantity = it.quantity - 1
                        )
                    )
                },
                onAddUnitPressed = {
                    showBottomSheet = showBottomSheet.copy(
                        second = it.copy(
                            quantity = it.quantity + 1
                        )
                    )
                },

                )
        }
    }
}


@Composable
@Preview()
fun CheckoutScreenPreview() {
    MaterialTheme {
        Surface {
            CheckoutScreen(
                onBackPressed = { },
                onCheckoutPressed = { },
            )
        }
    }
}