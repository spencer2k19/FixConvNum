package com.yanncer.fixconvnum.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yanncer.fixconvnum.presentation.home.HomeView
import com.yanncer.fixconvnum.presentation.ui.theme.FixConvNumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConvNumApp()
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun ConvNumApp() {
    FixConvNumTheme {
        val navController = rememberNavController()
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize()

        ) {
            NavHost(navController = navController, startDestination = Home.route) {
                composable(Home.route) {
                    HomeView(navController = navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FixConvNumTheme {
       ConvNumApp()
    }
}