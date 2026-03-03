package com.aodapp

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class AODActivity : AppCompatActivity() {

    private lateinit var clockText: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mostrar encima del lockscreen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)

        startClock()
        startBurnInProtection()
    }

    private fun startClock() {
        handler.post(object : Runnable {
            override fun run() {
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                clockText.text = sdf.format(Date())
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun startBurnInProtection() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                moveTextSlightly()
                handler.postDelayed(this, 10000)
            }
        }, 10000)
    }

    private fun moveTextSlightly() {
        val randomX = Random.nextInt(-20, 20).toFloat()
        val randomY = Random.nextInt(-20, 20).toFloat()

        clockText.translationX = randomX
        clockText.translationY = randomY
    }

    override fun onBackPressed() {
        // Bloquear botón atrás
    }
}
