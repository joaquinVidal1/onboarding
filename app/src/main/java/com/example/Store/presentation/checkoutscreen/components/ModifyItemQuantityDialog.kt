package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Store.presentation.common.components.ProductRow
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ModifyProductQuantityDialog(
    productQty: Int,
    product: Product,
    onDisMissDialog: () -> Unit,
    onAddUnitPressed: () -> Unit,
    onRemoveUnitPressed: () -> Unit,
    onEditQuantityConfirmed: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDisMissDialog,
        sheetState = sheetState
    ) {

        Column(modifier = Modifier.padding(horizontal = 18.dp)) {

            Text(
                text = stringResource(id = R.string.bottom_sheet_title),
                style = MaterialTheme.typography.h1,
                fontSize = 18.sp,
                modifier = Modifier.align(CenterHorizontally)
            )

            Spacer(modifier = Modifier.size(24.dp))

            ProductRow(
                product = product,
                quantity = productQty,
                onAddUnitPressed = onAddUnitPressed,
                onRemoveUnitPressed = onRemoveUnitPressed
            )

            Spacer(modifier = Modifier.size(24.dp))

            ConfirmationFABButton(
                text = stringResource(id = R.string.confirm),
                isEnabled = true,
                onButtonPressed = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onEditQuantityConfirmed()
                        }
                    }
                },
                enabledColor = colorResource(id = R.color.confirmation_green),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.size(44.dp))
        }

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