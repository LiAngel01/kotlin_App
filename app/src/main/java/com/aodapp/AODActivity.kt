package com.aodapp

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class AODActivity : Activity() {

    private lateinit var clockText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView
    private lateinit var chargeTimeText: TextView

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)
        dateText = findViewById(R.id.dateText)
        batteryText = findViewById(R.id.batteryText)
        chargeTimeText = findViewById(R.id.chargeTimeText)

        updateInfo()
    }

    private fun updateInfo() {

        handler.post(object : Runnable {

            override fun run() {

                val now = Date()

                val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
                val date = SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(now)

                clockText.text = time
                dateText.text = date

                val batteryStatus = registerReceiver(
                    null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )

                val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

                val percent = level * 100 / scale

                batteryText.text = "Battery: $percent%"

                val chargeTime = batteryStatus?.getIntExtra(
                    BatteryManager.EXTRA_CHARGE_TIME_REMAINING,
                    -1
                ) ?: -1

                if (chargeTime > 0) {

                    val minutes = chargeTime / 60000

                    chargeTimeText.text = "Full in $minutes min"

                } else {

                    chargeTimeText.text = "Charging..."

                }

                handler.postDelayed(this, 30000)

            }

        })

    }

}
