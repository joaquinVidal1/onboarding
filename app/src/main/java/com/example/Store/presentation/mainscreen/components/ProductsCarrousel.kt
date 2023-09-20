package com.example.proyecto_final_de_onboarding.presentation.mainscreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.CarrouselPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsCarrousel(
    pages: List<CarrouselPage>, modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    HorizontalPager(
        state = pagerState, modifier = modifier
    ) { pagePosition ->
        val page = pages[pagePosition]
        CarrouselBanner(
            image = page.image,
            title = page.title,
            subtitle = page.subtitle,
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )
    }
}

@Composable
@Preview
fun ProductsCarrouselPreview() {
    MaterialTheme {
        ProductsCarrousel(
            pages = listOf(
                CarrouselPage(
                    title = "Brazilian Bananas",
                    subtitle = "Product of the month",
                    image = R.drawable.banner_1
                )
            ), modifier = Modifier.wrapContentSize()
        )
    }
}