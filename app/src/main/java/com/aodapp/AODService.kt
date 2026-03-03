package com.aodapp

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.TextView
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

class AODService : Service() {

    companion object {
        private var windowManager: WindowManager? = null
        private var overlayView: View? = null

        fun start(context: Context) {
            val intent = Intent(context, AODService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, AODService::class.java))
        }

        fun showOverlay(context: Context) {

    if (overlayView != null) return

    windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    val inflater = LayoutInflater.from(context)
    overlayView = inflater.inflate(R.layout.overlay_layout, null)

    val params = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        PixelFormat.OPAQUE
    )

    windowManager?.addView(overlayView, params)
    Settings.System.putInt(
    context.contentResolver,
    Settings.System.SCREEN_BRIGHTNESS,
    1
    )

    val clockText = overlayView!!.findViewById<TextView>(R.id.clockText)

    val handler = android.os.Handler(android.os.Looper.getMainLooper())

    val updateClock = object : Runnable {
        override fun run() {

            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            clockText.text = time

            // Movimiento leve anti burn-in
            val randomX = (-20..20).random().toFloat()
            val randomY = (-20..20).random().toFloat()
            clockText.translationX = randomX
            clockText.translationY = randomY

            handler.postDelayed(this, 60000) // 1 minuto
        }
    }

    updateClock.run()
}

        fun hideOverlay() {
            overlayView?.let {
                windowManager?.removeView(it)
                overlayView = null
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val channelId = "AOD_CHANNEL"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "AOD Service",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("AOD Running")
            .setContentText("Waiting for screen off...")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
