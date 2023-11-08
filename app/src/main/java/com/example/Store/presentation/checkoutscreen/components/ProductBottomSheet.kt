package com.example.Store.presentation.checkoutscreen.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.Store.presentation.common.components.ProductRow
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    product: Product,
    quantity: Int,
    onAddUnit: () -> Unit,
    onRemoveUnit: () -> Unit,
    onEditConfirmed: () -> Unit,
    onDismissDialog: () -> Unit
) {

    ModalBottomSheet(onDismissRequest = onDismissDialog) {
        Text(
            text = stringResource(id = R.string.dialog_title),
            style = MaterialTheme.typography.h1
        )

        Spacer(modifier = Modifier.size(24.dp))

        ProductRow(
            product = product,
            quantity = quantity,
            onAddUnitPressed = onAddUnit,
            onRemoveUnitPressed = onRemoveUnit
        )

        Spacer(modifier = Modifier.size(24.dp))

        ConfirmationFABButton(
            text = stringResource(id = R.string.confirm),
            isEnabled = true,
            onButtonPressed = onEditConfirmed,
            enabledColor = colorResource(id = R.color.confirmation_green),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(44.dp))
    }
}