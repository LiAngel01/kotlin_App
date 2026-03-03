package com.aodapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var enableAODButton: Button

    private var isAODEnabled = false

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            toggleAOD()
        } else {
            Toast.makeText(this, "Notification permission required", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        enableAODButton = findViewById(R.id.enableAODButton)

        enableAODButton.setOnClickListener {
            checkPermissionAndToggle()
        }

        updateStatus()
    }

    private fun checkPermissionAndToggle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    toggleAOD()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            toggleAOD()
        }
    }

    private fun toggleAOD() {
        if (isAODEnabled) {
            AODService.stop(this)
            isAODEnabled = false
            Toast.makeText(this, "AOD Disabled", Toast.LENGTH_SHORT).show()
        } else {
            AODService.start(this)
            isAODEnabled = true
            Toast.makeText(this, "AOD Enabled - Lock screen to see it", Toast.LENGTH_LONG).show()
        }
        updateStatus()
    }

    private fun updateStatus() {
        statusText.text = if (isAODEnabled) {
            "AOD Active"
        } else {
            "AOD Disabled"
        }

        enableAODButton.text = if (isAODEnabled) {
            "Disable AOD"
        } else {
            "Enable AOD"
        }
    }
}
