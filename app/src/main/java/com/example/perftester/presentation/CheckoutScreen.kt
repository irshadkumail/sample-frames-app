package com.example.perftester.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen() {

    Scaffold(
        modifier = Modifier.recordFrameRate(LocalContext.current as Activity, "CheckoutScreen"),
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Checkout"
                )
            })

        },
        content = { paddingValues ->

            val viewModel = hiltViewModel<MainViewModel>()

            Box(modifier = Modifier.padding(paddingValues)) {

            }
        }
    )
}