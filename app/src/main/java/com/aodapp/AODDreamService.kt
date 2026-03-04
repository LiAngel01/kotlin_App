package com.aodapp

import android.service.dreams.DreamService
import android.view.View
import android.widget.TextView
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.*

class AODDreamService : DreamService() {

    private lateinit var clockText: TextView
    private val handler = Handler(Looper.getMainLooper())

    private val updateRunnable = object : Runnable {
        override fun run() {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            clockText.text = timeFormat.format(Date())
            handler.postDelayed(this, 60000)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isInteractive = false
        isFullscreen = true
        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)
        handler.post(updateRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(updateRunnable)
    }
}
