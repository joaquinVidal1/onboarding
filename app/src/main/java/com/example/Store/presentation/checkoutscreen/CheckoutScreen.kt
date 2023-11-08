package com.example.Store.presentation.checkoutscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.Store.presentation.checkoutscreen.components.CartItem
import com.example.Store.presentation.checkoutscreen.components.ConfirmationFABButton
import com.example.Store.presentation.checkoutscreen.components.ProductBottomSheet
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem

@Composable
fun CheckoutScreen(
    viewModel: CheckoutScreenViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {

    val cart by viewModel.screenList.collectAsState(initial = listOf())
    val totalAmount by viewModel.totalAmount.collectAsState()
    val isCheckoutEnabled by viewModel.showCheckoutButton.collectAsState(initial = true)

    var showBottomSheet: ScreenListItem.ScreenItem? by remember {
        mutableStateOf(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                painter = painterResource(id = R.drawable.icon_back),
                contentDescription = null,
                tint = colorResource(id = R.color.item_name_color)
            )
        }

        Text(
            text = stringResource(id = R.string.shopping_cart),
            style = MaterialTheme.typography.h1,
            fontSize = 22.sp
        )

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
            content = {
                items(cart) { carItem ->
                    CartItem(item = carItem, modifier = Modifier.clickable {
                        showBottomSheet = carItem
                    })
                }
            })

        Column(modifier = Modifier.weight(0.3f)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(id = R.string.total),
                    style = MaterialTheme.typography.h1,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp
                )

                Text(
                    text = stringResource(id = R.string.price, totalAmount),
                    style = MaterialTheme.typography.h1,
                    fontSize = 32.sp
                )
            }

            ConfirmationFABButton(
                text = stringResource(id = R.string.checkout),
                isEnabled = isCheckoutEnabled,
                onButtonPressed = {
                    viewModel.cleanCart()
                    onBackPressed()
                },
                enabledColor = colorResource(id = R.color.color_checkout_button),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            )

            Spacer(modifier = Modifier.size(44.dp))


        }
    }

    showBottomSheet?.let {
        ProductBottomSheet(product = it.product,
            quantity = it.quantity,
            onAddUnit = {
                showBottomSheet = it.copy(quantity = it.quantity + 1)
            },
            onRemoveUnit = {
                showBottomSheet = it.copy(quantity = it.quantity - 1)
            },
            onEditConfirmed = {
                viewModel.itemQtyChanged(
                    it.id, it.quantity
                )
                showBottomSheet = null
            },
            onDismissDialog = {
                showBottomSheet = null
            })
    }
}

@Composable
@Preview(showBackground = true)
fun CheckoutScreenPreview() {

        CheckoutScreen(onBackPressed = {})

}