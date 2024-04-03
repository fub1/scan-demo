package com.crtyiot.ssd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.crtyiot.ssd.com.crtyiot.ssd.MainScreen
import com.crtyiot.ssd.com.crtyiot.ssd.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel = MainViewModel())
        }
    }
}

