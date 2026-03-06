package com.aodapp

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AODActivity : Activity() {

    private lateinit var clockText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aod)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        clockText = findViewById(R.id.clockText)
        dateText = findViewById(R.id.dateText)
        batteryText = findViewById(R.id.batteryText)

        updateClock()
    }

    private fun updateClock() {

        handler.post(object : Runnable {

            override fun run() {

                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val date = SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date())

                clockText.text = time
                dateText.text = date

                updateBattery()

                handler.postDelayed(this, 60000)
            }
        })
    }

    private fun updateBattery() {

        val batteryIntent = registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)

        batteryText.text = "Battery $level%"
    }
}
