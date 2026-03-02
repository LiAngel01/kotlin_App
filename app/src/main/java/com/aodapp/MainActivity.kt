package com.aodapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "aod_service_channel"
    }

    private lateinit var statusText: TextView
    private lateinit var enableAODButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        enableAODButton = findViewById(R.id.enableAODButton)

        createNotificationChannel()
        updateStatus()

        enableAODButton.setOnClickListener {
            openAODSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
    }

    private fun updateStatus() {
        val isAODEnabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.SCREENSAVER_ENABLED
        ) == "1"

        if (isAODEnabled) {
            statusText.text = getString(R.string.aod_enabled)
            statusText.setTextColor(getColor(R.color.green))
        } else {
            statusText.text = getString(R.string.aod_disabled)
            statusText.setTextColor(getColor(R.color.red))
        }
    }

    private fun openAODSettings() {
        try {
            // Open Dreams settings (AOD)
            startActivity(Intent(Settings.ACTION_DREAM_SETTINGS))
        } catch (e: Exception) {
            // Fallback to screensaver settings
            startActivity(Intent(Settings.ACTION_SCREENSAVER_SETTINGS))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "AOD Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps Always On Display running"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
