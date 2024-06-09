package com.example.perftester.presentation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.perftester.R

@Composable
fun WelcomeScreen(navController: NavController) {

    Column(
        modifier = Modifier.recordFrameRate(LocalContext.current as Activity, "WelcomeScreen"),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.mat),
            contentDescription = "Welcome icon"
        )

        Text(
            text = "Welcome to the test app",
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This app is developed by Irshad Kumail to test frame metrics with compose navigation. It helps us test and check number ",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            content = {
                Text(text = "Lets Go")
            }, onClick = {
                navController.navigate(Routes.LIST.name)
            }
        )

    }

}