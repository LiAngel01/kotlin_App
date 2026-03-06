package com.aodapp

import android.service.dreams.DreamService
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AODDreamService : DreamService() {

    private lateinit var clockText: TextView

    private val handler = Handler(Looper.getMainLooper())

    override fun onAttachedToWindow() {

        super.onAttachedToWindow()

        setContentView(R.layout.activity_aod)

        setInteractive(false)
        setFullscreen(true)

        clockText = findViewById(R.id.clockText)

        updateClock()
    }

    private fun updateClock() {

        handler.post(object : Runnable {

            override fun run() {

                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

                clockText.text = time

                handler.postDelayed(this, 60000)
            }
        })
    }
}
