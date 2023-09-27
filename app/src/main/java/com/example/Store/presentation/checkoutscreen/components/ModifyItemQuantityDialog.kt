package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModifyProductQuantityDialog(
    productQty: Int, product: Product, onDisMissDialog: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDisMissDialog) {

    }
}

@Preview
@Composable
fun ModifyItemQuantityDialogPreview() {
    MaterialTheme {
        ModifyProductQuantityDialog(productQty = 1, product = Product(
            id = 1,
            name = "Kiwi",
            price = 5.0,
            mainImage = "",
            checkoutImage = "",
            kind = Kind.Fruit
        ), onDisMissDialog = {})
    }
}