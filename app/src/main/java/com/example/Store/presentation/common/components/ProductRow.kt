package com.example.Store.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.Store.presentation.StoreTheme
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.Kind
import com.example.proyecto_final_de_onboarding.domain.model.Product

@Composable
fun ProductRow(
    product: Product,
    quantity: Int,
    onAddUnitPressed: () -> Unit,
    onRemoveUnitPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val placeholderImage = painterResource(id = R.drawable.placeholder)

        AsyncImage(
            model = product.mainImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = placeholderImage,
            error = placeholderImage,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.size(24.dp))

        Column {
            Text(text = product.name, style = MaterialTheme.typography.h1)

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = stringResource(
                    id = R.string.price, product.roundedPrice
                ), style = MaterialTheme.typography.h2
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        AddButton(
            qty = quantity,
            onAddUnitPressed = onAddUnitPressed,
            onRemoveUnitPressed = onRemoveUnitPressed,
            modifier = Modifier.width(100.dp)
        )
    }
}

@Composable
@Preview
fun productRowPreview() {
    StoreTheme {
        ProductRow(
            product = Product(
                id = 1,
                name = "Kiwi",
                price = 5.0,
                mainImage = "",
                checkoutImage = "",
                kind = Kind.Fruit
            ),
            quantity = 1,
            onRemoveUnitPressed = { },
            onAddUnitPressed = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}