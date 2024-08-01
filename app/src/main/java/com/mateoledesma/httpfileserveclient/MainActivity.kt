package com.mateoledesma.httpfileserveclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mateoledesma.httpfileserveclient.ui.App
import com.mateoledesma.httpfileserveclient.ui.theme.HTTPFileServeClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HTTPFileServeClientTheme {
                App()
            }
        }
    }
}
