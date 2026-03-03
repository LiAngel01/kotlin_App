package com.aodapp

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class AODActivity : Activity() {

    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var batteryText: TextView
    private lateinit var container: FrameLayout
    
    private val handler = Handler(Looper.getMainLooper())
    private var isScreenOn = false
    
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateTimeAndDate()
            // Update every second
            handler.postDelayed(this, 1000)
        }
    }

    private val moveTextRunnable = object : Runnable {
        override fun run() {
            // Small position change every 30 seconds to prevent burn-in
            moveTextSlightly()
            handler.postDelayed(this, 30000)
        }
    }

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                isScreenOn = true
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Keep screen off but show our AOD
        setupWindow()
        
        setContentView(R.layout.aod_layout)
        
        setupViews()
        registerScreenReceiver()
        
        // Start updating
        updateTimeAndDate()
        handler.post(updateTimeRunnable)
        handler.post(moveTextSlightly)
    }

    private fun setupWindow() {
        // Keep screen on (but dim)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Show on top of lock screen
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        
        // Full screen
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
        
        // Black background
        window.statusBarColor = android.graphics.Color.BLACK
        window.navigationBarColor = android.graphics.Color.BLACK
    }

    private fun setupViews() {
        timeText = findViewById(R.id.aodTime)
        dateText = findViewById(R.id.aodDate)
        batteryText = findViewById(R.id.aodBattery)
        container = findViewById(R.id.aodContainer)
    }

    private fun registerScreenReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(screenReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(screenReceiver, filter)
        }
    }

    private fun updateTimeAndDate() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
        
        val now = Date()
        timeText.text = timeFormat.format(now)
        dateText.text = dateFormat.format(now)
        
        // Update battery
        updateBattery()
    }

    private fun updateBattery() {
        try {
            val batteryIntent = registerReceiver(null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED))
            val level = batteryIntent?.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryIntent?.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1) ?: -1
            
            if (level >= 0 && scale > 0) {
                val battery = (level * 100) / scale
                val charging = batteryIntent?.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1) 
                    == android.os.BatteryManager.BATTERY_STATUS_CHARGING
                
                batteryText.text = if (charging) "⚡ $battery%" else "$battery%"
            }
        } catch (e: Exception) {
            batteryText.text = "--%"
        }
    }

    private fun moveTextSlightly() {
        // Small random movement to prevent AMOLED burn-in
        // Movement range: -5 to +5 pixels
        val offsetX = Random.nextInt(-5, 6)
        val offsetY = Random.nextInt(-5, 6)
        
        val params = timeText.layoutParams as? FrameLayout.LayoutParams
        params?.let {
            it.gravity = Gravity.CENTER or Gravity.CENTER
            // Apply small offset
            it.leftMargin = offsetX
            it.topMargin = offsetY
            timeText.layoutParams = it
        }
        
        // Also move date slightly
        val dateParams = dateText.layoutParams as? FrameLayout.LayoutParams
        dateParams?.let {
            it.leftMargin = offsetX
            it.topMargin = offsetY
            dateText.layoutParams = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable)
        handler.removeCallbacks(moveTextRunnable)
        try {
            unregisterReceiver(screenReceiver)
        } catch (e: Exception) {}
    }

    companion object {
        private var instance: AODActivity? = null
        
        fun show(context: Context) {
            val intent = Intent(context, AODActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            context.startActivity(intent)
        }
        
        fun close() {
            instance?.finish()
            instance = null
        }
    }
}
