package com.aodapp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var receiver: ScreenReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)

        receiver = ScreenReceiver()

        startButton.setOnClickListener {

            val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)

            registerReceiver(receiver, filter)
        }
    }
}
