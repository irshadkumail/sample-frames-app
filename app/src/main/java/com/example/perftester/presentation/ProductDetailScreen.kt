package com.example.perftester.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController, productIndex: String
) {
    Scaffold(
        modifier = Modifier.recordFrameRate(LocalContext.current as Activity, "ProductDetailScreen"),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Product Detail"
                )
            })

        }, content = { paddingValues ->

            val viewModel = hiltViewModel<MainViewModel>()
            val product = viewModel.findSelectedItem(productIndex.toInt())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    AsyncImage(
                        model = product?.image,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(420.dp),
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(text = product?.title ?: "", fontWeight = FontWeight.Bold)
                }
                Button(modifier = Modifier
                    .fillMaxWidth(),
                    onClick = { navController.navigate(Routes.CHECKOUT.label) },
                    content = {
                        Text(text = "Confirm")
                    }
                )

            }
        })

}