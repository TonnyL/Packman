package com.lizhaotailang.packman.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.lizhaotailang.packman.common.ui.Screen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalNavController provides navController
            ) {
                MaterialTheme {
                    MainNavHost(startDestination = Screen.HomeScreen)
                }
            }
        }
    }
}
