package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.Store.presentation.common.components.ProductRow
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyProductQuantityDialog(
    productQty: Int,
    product: Product,
    onDisMissDialog: () -> Unit,
    onAddUnitPressed: () -> Unit,
    onRemoveUnitPressed: () -> Unit,
    onEditQuantityConfirmed: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDisMissDialog) {
        ProductRow(
            product = product,
            quantity = productQty,
            onAddUnitPressed = onAddUnitPressed,
            onRemoveUnitPressed = onRemoveUnitPressed
        )
    }
}

@Preview
@Composable
fun ModifyItemQuantityDialogPreview() {
    MaterialTheme {
        ModifyProductQuantityDialog(productQty = 1,
            product = Product(
                id = 1,
                name = "Kiwi",
                price = 5.0,
                mainImage = "",
                checkoutImage = "",
                kind = Kind.Fruit
            ),
            onDisMissDialog = {},
            onAddUnitPressed = { },
            onRemoveUnitPressed = { },
            onEditQuantityConfirmed = {})
    }
}