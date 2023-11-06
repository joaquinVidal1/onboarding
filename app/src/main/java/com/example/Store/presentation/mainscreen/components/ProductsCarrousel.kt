package com.example.Store.presentation.mainscreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.Store.domain.model.CarrouselPage
import com.example.proyecto_final_de_onboarding.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsCarrousel(
    pages: List<CarrouselPage>, modifier: Modifier = Modifier
) {
    val pageCount = pages.size
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxWidth()
        ) { pagePosition ->
            val page = pages[pagePosition]
            CarrouselBanner(
                image = page.image,
                title = stringResource(id = page.title),
                subtitle = stringResource(id = page.subtitle),
                modifier = modifier
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Row {
            repeat(pageCount) { currentIteration ->
                val color =
                    colorResource(id = if (pagerState.currentPage == currentIteration) R.color.color_dot_selected else R.color.color_dot_unselected)
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)

                )
            }
        }

    }
}

@Composable
@Preview
fun ProductsCarrouselPreview() {
    MaterialTheme {
        ProductsCarrousel(
            pages = listOf(
                CarrouselPage(
                    title = R.string.brazilian_bananas,
                    subtitle = R.string.product_of_the_month,
                    image = R.drawable.banner_1
                )
            ), modifier = Modifier
                .padding(horizontal = 18.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
        )
    }
}