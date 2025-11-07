package ru.itis.hw_3

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.itis.hw_3.navigation.BottomNavigation
import ru.itis.hw_3.ui.theme.HW_3Theme
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.itis.hw_3.domain.model.BroadcastConstants
import ru.itis.hw_3.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    private val replyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == BroadcastConstants.ACTION_REPLY_RECEIVED) {
                val message = intent.getStringExtra(BroadcastConstants.EXTRA_MESSAGE)
                message?.let {
                    sharedViewModel.addMessage(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                123
            )
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            replyReceiver,
            IntentFilter(BroadcastConstants.ACTION_REPLY_RECEIVED)
        )

        setContent {
            HW_3Theme {
                BottomNavigation(
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(replyReceiver)
    }
}
