package com.example.kpv

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class FirstActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstActivityScreen()
        }
    }
}

@Composable
fun FirstActivityScreen() {
    var text by remember { mutableStateOf("")}
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Экран 1",
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        TextField(
            value = text,
            onValueChange = {newText ->
                text = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            singleLine = true
        )

        Button(
            onClick = {
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra("ENTERED_TEXT_2", text)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(
                text = "Перейти на экран 2"
            )
        }

        Button(
            onClick = {
                val intent = Intent(context, ThirdActivity::class.java)
                intent.putExtra("ENTERED_TEXT", text)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Text(
                text = "Перейти на экран 3"
            )
        }
    }
}