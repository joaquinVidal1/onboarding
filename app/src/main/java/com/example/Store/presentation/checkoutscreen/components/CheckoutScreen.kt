package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.Store.presentation.checkoutscreen.CheckoutScreenViewModel

@Composable
fun CheckoutScreen() {
    val viewModel: CheckoutScreenViewModel = hiltViewModel()
    val cart by viewModel.screenList.collectAsState(initial = listOf())
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        state = listState,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        items(items = cart, key = { it.product.id }) {
            CartItem(
                item = it,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(150.dp)
            )
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