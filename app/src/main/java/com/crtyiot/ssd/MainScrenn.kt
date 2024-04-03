package com.crtyiot.ssd.com.crtyiot.ssd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.lang.IllegalArgumentException


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val isenable by viewModel.scanstate.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (isenable) {
            Text(text = "yes")
            InputFx(viewModel)
        }
        Button(onClick = { viewModel.changex() }) {
            Text(text = "Woo..")
        }
    }
}

@Composable
fun InputFx(
    viewModel: MainViewModel,
    //https://developer.android.com/develop/ui/compose/side-effects?hl=zh-cn#disposableeffect
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    val inputText by viewModel.inputData.collectAsState()
    val scanText by viewModel.scanData.collectAsState()
    val scanError by viewModel.scanError.collectAsState()

    TextField(
        value = inputText,
        onValueChange = { viewModel.addInputData(it) },
        label = { Text("input Data") },
        )

    TextField(
        value = inputText,
        onValueChange = { viewModel.addInputData(it) },
        label = { Text("input Data copy") },
        enabled = false
    )
    Log.i("Screen build", "scanError: $scanError")
    if (scanError) { TextField(
            value = scanText,
            onValueChange = { },
            label = { Text("Scan_DT") },
            isError = true,
            enabled = false)
    } else {
        TextField(
            value = scanText,
            onValueChange = { },
            label = { Text("Scan_DT") },
            isError = false,
            enabled = true)
    }



    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnStop by rememberUpdatedState(onStop)
    // 广播使用的上下文
    val context = LocalContext.current



    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // 广播
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.i("Bc", "onReceive")
                val scanData = intent?.getStringExtra("com.symbol.datawedge.data_string")
                scanData?.let { viewModel.addScanData(it) }
            }
        }
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.i("MainScreen", "ON_START")
                currentOnStart()
                context.registerReceiver(receiver, IntentFilter("com.v.b"))
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.i("MainScreen", "ON_STOP")
                try {
                    context.unregisterReceiver(receiver)
                } catch (e: IllegalArgumentException) {
                    Log.e("MainScreen-when stop", "unregisterReceiver error")
                }
                currentOnStop()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Log.e("MainScreen-when Dispose", "unregisterReceiver error")
            }
            Log.i("MainScreen", "onDispose")
        }
    }


}




