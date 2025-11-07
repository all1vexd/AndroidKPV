package ru.itis.hw_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.itis.hw_2.navigation.NavGraph
import ru.itis.hw_2.ui.theme.My_Theme
import ru.itis.hw_2.viewModels.ThemeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val themeViewModel: ThemeViewModel = viewModel()
            val currentTheme = themeViewModel.currentTheme

            My_Theme(appTheme = currentTheme.value) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier.padding(innerPadding),
                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}
