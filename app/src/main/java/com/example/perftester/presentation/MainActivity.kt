package com.example.perftester.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.perftester.client.Product
import com.example.perftester.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme

                val navController = rememberNavController()


                NavHost(navController = navController, startDestination = Routes.WELCOME.label) {
                    composable(Routes.WELCOME.label) {
                        WelcomeScreen(navController = navController)
                    }
                    composable(Routes.LIST.label) {
                        ProductListScreen(navController)
                    }
                    composable(
                        Routes.DETAIL.label,
                        arguments = listOf(navArgument("index", { type = NavType.StringType }))
                    ) {
                        val productIndex = it.arguments?.getString("index") ?: ""
                        ProductDetailScreen(navController, productIndex)
                    }
                    composable(Routes.CHECKOUT.label) {
                        CheckoutScreen()
                    }

                }

                /*      Surface(
                          modifier = Modifier.fillMaxSize().recordFrameRate(activity, "MainActivity"),
                          color = MaterialTheme.colorScheme.background
                      ) {
                          Column {

                              val context = LocalContext.current

                              Button(onClick = {
                                  startActivity(
                                      Intent(
                                          context,
                                          BlankActivity::class.java
                                      )
                                  )
                              }) {
                                  Text(text = "Go")
                              }
                              ProductListView(viewModel.uiState)
                          }
                      }*/
            }
        }
    }
}

enum class Routes(val label: String) {
    WELCOME("welcome"),
    LIST("list"),
    DETAIL("detail/{index}"),
    CHECKOUT("checkout"),
}


