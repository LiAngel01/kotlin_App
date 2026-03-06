package com.aodapp

import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AODActivity : AppCompatActivity() {

    private lateinit var clockText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView
    private lateinit var contentLayout: LinearLayout

    private val handler = Handler(Looper.getMainLooper())

    private val timeRunnable = object : Runnable {
        override fun run() {
            updateTime()
            moveSlightly()
            handler.postDelayed(this, 60000) // solo 1 vez por minuto
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        window.addFlags(
        android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
        android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
        android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)
        dateText = findViewById(R.id.dateText)
        batteryText = findViewById(R.id.batteryText)
        contentLayout = findViewById(R.id.contentLayout)

        updateTime()
        registerBatteryReceiver()

        handler.post(timeRunnable)
    }

    private fun updateTime() {
        val now = Date()

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

        clockText.text = timeFormat.format(now)
        dateText.text = dateFormat.format(now)
    }

    private fun moveSlightly() {
        contentLayout.translationX = (-3..3).random().toFloat()
        contentLayout.translationY = (-3..3).random().toFloat()
    }

    private fun registerBatteryReceiver() {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                batteryText.text = "$level%"

                if (level in 0..14) {
                    finish() // se apaga si batería < 15%
                }
            }
        }, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRunnable)
    }
}
