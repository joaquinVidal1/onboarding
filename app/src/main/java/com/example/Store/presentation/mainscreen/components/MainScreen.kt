package com.example.Store.presentation.mainscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.Store.presentation.StoreTheme
import com.example.Store.presentation.common.components.ProductRow
import com.example.Store.presentation.mainscreen.MainScreenViewModel
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.model.ScreenListItem
import com.example.proyecto_final_de_onboarding.presentation.mainscreen.components.ProductsCarrousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCartPressed: () -> Unit
) {

    val viewModel: MainScreenViewModel = hiltViewModel()
    val carrouselPages by viewModel.carrouselPages.collectAsState(initial = listOf())
    val query by viewModel.query.collectAsState(initial = "")
    val products by viewModel.displayList.collectAsState(initial = listOf())

    val horizontalPadding = 18.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.color_background_app))
            .padding(top = 12.dp)
    ) {

        IconButton(
            onClick = onCartPressed, modifier = Modifier
                .align(
                    Alignment.End
                )
                .padding(end = 30.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_cart),
                contentDescription = null,
            )
        }

        ProductsCarrousel(
            pages = carrouselPages,
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )

        Spacer(modifier = Modifier.size(24.dp))

        SearchBar(
            query = query,
            onQueryChange = { newQuery -> viewModel.onQueryChanged(newQuery) },
            onSearch = {},
            active = false,
            onActiveChange = {},
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_search),
                    contentDescription = null,
                    tint = colorResource(
                        id = R.color.search_text_color
                    )
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search),
                    color = colorResource(
                        id = R.color.search_text_color
                    )
                )
            },
            colors = SearchBarDefaults.colors(containerColor = colorResource(id = R.color.backgrouns_search_color)),
            modifier = Modifier.padding(horizontal = horizontalPadding)
        ) {}

        LazyColumn(
            contentPadding = PaddingValues(
                top = 24.dp,
                start = horizontalPadding,
                end = horizontalPadding
            )
        ) {

            items(items = products) { cartItem ->
                if (cartItem is ScreenListItem.ScreenItem) {
                    ProductRow(
                        product = cartItem.product,
                        quantity = cartItem.quantity,
                        onAddUnitPressed = { viewModel.onAddItem(cartItem.id) },
                        onRemoveUnitPressed = { viewModel.onRemoveItem(cartItem.id) },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Divider(
                        color = colorResource(id = R.color.borders_main_screenItems),
                        modifier = Modifier.padding(horizontal = horizontalPadding)
                    )
                } else {
                    Text(
                        text = (cartItem as ScreenListItem.ScreenHeader).kind.name,
                        style = MaterialTheme.typography.h1,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }

}

@Composable
@Preview
fun MainScreenPreview() {
    StoreTheme {
        MainScreen {

        }
    }
}