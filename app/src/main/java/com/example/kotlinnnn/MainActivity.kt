package com.example.kotlinnnn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CoroutinesScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp,
                            top = 266.dp,
                            end = 16.dp,
                            bottom = 16.dp)
                )
            }
//            MaterialTheme {
//                FlowScreen(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(start = 16.dp,
//                            top = 266.dp,
//                            end = 16.dp,
//                            bottom = 16.dp)
//                )
//            }
//            MaterialTheme {
//                StateFlowScreen(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(start = 16.dp,
//                            top = 266.dp,
//                            end = 16.dp,
//                            bottom = 16.dp)
//                )
//            }
//            MaterialTheme {
//                SharedFlowScreen(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(start = 16.dp,
//                            top = 266.dp,
//                            end = 16.dp,
//                            bottom = 16.dp)
//                )
//            }
//            MaterialTheme {
//                ErrorHandlingScreen(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(start = 16.dp,
//                            top = 266.dp,
//                            end = 16.dp,
//                            bottom = 16.dp)
//                )
//            }
        }
    }
}