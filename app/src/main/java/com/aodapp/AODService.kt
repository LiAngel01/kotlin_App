package com.aodapp

import android.service.dreams.DreamService
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AODService : DreamService() {

    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateTimeAndDate()
            batteryText.postDelayed(this, 60000) // Update every minute
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // Configure the dream
        setContentView(R.layout.aod_layout)
        
        // Find views
        timeText = findViewById(R.id.aodTime)
        dateText = findViewById(R.id.aodDate)
        batteryText = findViewById(R.id.aodBattery)

        // Allow tapping to wake up
        setInteractive(true)
        
        // Don't show system UI
        setFullscreen(true)

        // Start updating time
        updateTimeAndDate()
        timeText.post(updateRunnable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        timeText.removeCallbacks(updateRunnable)
    }

    private fun updateTimeAndDate() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())

        val now = Date()
        timeText.text = timeFormat.format(now)
        dateText.text = dateFormat.format(now)

        // Update battery if available
        val batteryIntent = registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1) ?: -1
        
        if (level >= 0 && scale > 0) {
            val battery = (level * 100) / scale
            batteryText.text = "$battery%"
        }
    }

    override fun onDreamingStarted() {
        super.onDreamingStarted()
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
    }
}
