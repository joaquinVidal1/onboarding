package com.example.Store.presentation.mainscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.Store.presentation.StoreTheme
import com.example.Store.presentation.mainscreen.MainScreenViewModel
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.presentation.mainscreen.components.ProductsCarrousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCartPressed: () -> Unit
) {

    val viewModel: MainScreenViewModel = hiltViewModel()
    val carrouselPages by viewModel.carrouselPages.collectAsState(initial = listOf())
    val query by viewModel.query.collectAsState(initial = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.color_background_app))
            .padding(start = 18.dp, end = 18.dp, top = 12.dp)
    ) {

        IconButton(
            onClick = onCartPressed, modifier = Modifier
                .align(
                    Alignment.End
                )
                .padding(end = 11.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_cart),
                contentDescription = null,
            )
        }

        ProductsCarrousel(pages = carrouselPages)

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
        ) {}
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