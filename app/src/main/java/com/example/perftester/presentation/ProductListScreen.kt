package com.example.perftester.presentation

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.perftester.client.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController) {

    Scaffold(
        modifier = Modifier.recordFrameRate(LocalContext.current as Activity, "ProductListScreen"),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Product List"
                )
            })

        },
        content = { paddingValues ->

            val viewModel = hiltViewModel<MainViewModel>()

            Box(modifier = Modifier.padding(paddingValues)) {
                ProductListView(
                    navController = navController,
                    state = viewModel.uiState
                )
            }
        }
    )

}

@Composable
fun ProductListView(
    navController: NavController,
    state: ProductUIState
) {
    val viewModel = hiltViewModel<MainViewModel>()

    when (state) {
        is ProductUIState.Loading -> {

            Box {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(8.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        is ProductUIState.Success -> {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState
            ) {
                itemsIndexed(state.products) { index, product ->
                    ProductView(product = product) {
                        navController.navigate("detail/$index")
                    }
                }

                viewModel.updateStateIndex(listState.firstVisibleItemIndex)
            }
        }

        is ProductUIState.Error -> {
            Toast.makeText(LocalContext.current, "", Toast.LENGTH_LONG).show()
        }
    }

}


@Composable
fun ProductView(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(14.dp)
            .clickable {
                onClick.invoke()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        border = BorderStroke(1.dp, Color.Black),

        ) {

        AsyncImage(
            modifier = Modifier
                .size(140.dp)
                .align(Alignment.CenterHorizontally),
            model = product.image,
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(6.dp),
            text = product.title.orEmpty(),
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(6.dp),
            text = product.description.orEmpty(),
            fontWeight = FontWeight.Normal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(6.dp),
            text = "$ ${product.price.toString()}",
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp
        )

    }

}