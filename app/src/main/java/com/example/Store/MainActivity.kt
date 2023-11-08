package com.example.proyecto_final_de_onboarding

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import com.example.Store.presentation.StoreApp
import com.example.Store.presentation.StoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreTheme {
                Surface {
                    StoreApp()
                }
            }
        }
    }

}