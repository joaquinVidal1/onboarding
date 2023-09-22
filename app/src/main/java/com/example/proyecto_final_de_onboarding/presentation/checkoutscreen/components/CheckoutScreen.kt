package com.example.proyecto_final_de_onboarding.presentation.checkoutscreen.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_final_de_onboarding.presentation.checkoutscreen.CheckoutScreenViewModel

@Composable
fun CheckoutScreen() {
    val viewModel: CheckoutScreenViewModel = hiltViewModel()
    val cart by viewModel.screenList.observeAsState(initial = listOf())
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp), state = listState
    ) {
        items(items = cart, key = { it.product.id }) {
            CartItem(item = it)
        }
    }
}

@Composable
@Preview
fun CheckoutScreenPreview() {
    MaterialTheme {
        CheckoutScreen()
    }
}