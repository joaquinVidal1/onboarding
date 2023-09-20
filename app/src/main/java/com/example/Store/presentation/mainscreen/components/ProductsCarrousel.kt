package com.example.proyecto_final_de_onboarding.presentation.mainscreen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_final_de_onboarding.R
import com.example.proyecto_final_de_onboarding.domain.CarrouselPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsCarrousel(pages: List<CarrouselPage>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    HorizontalPager(state = pagerState, modifier = modifier.wrapContentSize()) { pagePosition ->
        val page = pages[pagePosition]
        Box {
            Image(
                painter = painterResource(id = page.image),
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .padding(start = 18.dp, bottom = 27.dp)
                    .wrapContentHeight()
                    .align(Alignment.BottomStart)
            ) {
                Text(text = page.subtitle, color = Color.White, fontSize = 14.sp)
                Text(
                    text = page.title,
                    color = Color.White,
                    modifier = Modifier.padding(top = 5.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
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
                    title = "Brazilian Bananas", subtitle = "Product of the month", image = R.drawable.banner_1
                )
            )
        )
    }
}