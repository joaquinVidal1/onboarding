package com.example.proyecto_final_de_onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("joaquin", "llego a main activity")
        setContentView(R.layout.activity_main)
    }
}