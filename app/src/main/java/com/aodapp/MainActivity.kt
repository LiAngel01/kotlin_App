package com.aodapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val startButton = findViewById<Button>(R.id.startButton)

        startButton.setOnClickListener {
            val intent = Intent(this, AODActivity::class.java)
            startActivity(intent)
        }
    }
}
