package com.example.perftester.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.perftester.EGRenderingRecorder
import com.example.perftester.ui.theme.MyApplicationTheme

class BlankActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                val activity = LocalContext.current as Activity

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text(
                        modifier = Modifier.recordFrameRate(activity,"BlankActivity"),
                        text = "Center"
                    )
                }
            }
        }
    }
}

@Composable
fun Modifier.recordFrameRate(activity: Activity, screenName: String): Modifier{
    val owner = LocalLifecycleOwner.current

    DisposableEffect(key1 = Unit) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    EGRenderingRecorder.startRecording(screenName, activity)
                }
                Lifecycle.Event.ON_PAUSE -> {
                    EGRenderingRecorder.stopRecording(screenName)
                }
                else -> {

                }
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            EGRenderingRecorder.stopRecording(screenName)
        }
    }

    return this
}


