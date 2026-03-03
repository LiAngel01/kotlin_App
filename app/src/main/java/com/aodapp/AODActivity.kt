package com.aodapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AODActivity : AppCompatActivity() {

    private lateinit var clockText: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pantalla completa
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Brillo mínimo
        val params = window.attributes
        params.screenBrightness = 0.01f
        window.attributes = params

        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)

        startClock()
    }

    private fun startClock() {
        val updateClock = object : Runnable {
            override fun run() {
                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                clockText.text = time

                // Movimiento leve anti burn-in
                val randomX = (-20..20).random().toFloat()
                val randomY = (-20..20).random().toFloat()
                clockText.translationX = randomX
                clockText.translationY = randomY

                handler.postDelayed(this, 60000) // cada minuto
            }
        }
        updateClock.run()
    }

    // Salir con doble toque
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            finish()
        }
        return super.onTouchEvent(event)
    }
}
