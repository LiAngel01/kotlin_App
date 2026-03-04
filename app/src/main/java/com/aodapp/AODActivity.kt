package com.aodapp

import android.content.*
import android.hardware.*
import android.os.*
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AODActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var clockText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var sensorManager: SensorManager
    private var motionSensor: Sensor? = null

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
            batteryText.text = "Battery $level%"

            if (level <= 15) {
                finish() // cerrar si batería baja
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val params = window.attributes
        params.screenBrightness = 0.01f
        window.attributes = params

        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        setContentView(R.layout.activity_aod)

        clockText = findViewById(R.id.clockText)
        dateText = findViewById(R.id.dateText)
        batteryText = findViewById(R.id.batteryText)

        startClock()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
        motionSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        checkNightMode()
    }

    private fun startClock() {
        val updateClock = object : Runnable {
            override fun run() {

                val now = Calendar.getInstance()

                clockText.text =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(now.time)

                dateText.text =
                    SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(now.time)

                clockText.translationX = (-20..20).random().toFloat()
                clockText.translationY = (-20..20).random().toFloat()

                val seconds = now.get(Calendar.SECOND)
                val delay = (60 - seconds) * 1000L

                handler.postDelayed(this, delay)
            }
        }
        updateClock.run()
    }

    private fun checkNightMode() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour in 22..23 || hour in 0..6) {
            clockText.setTextColor(0xFF008800.toInt())
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        finish() // salir si detecta movimiento significativo
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        sensorManager.unregisterListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        finish()
        return true
    }
}
