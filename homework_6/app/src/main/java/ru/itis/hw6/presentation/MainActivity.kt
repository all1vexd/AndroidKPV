package ru.itis.hw6.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.itis.hw6.domain.Song
import ru.itis.hw6.presentation.navigation.NavGraph
import ru.itis.hw6.presentation.ui.theme.HW6Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HW6Theme {
                NavGraph()
            }
        }
    }
}
