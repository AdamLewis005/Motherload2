package com.example.motherload2.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.motherload2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectButton : Button = findViewById(R.id.Conect)
        connectButton.setOnClickListener {
            val intent = Intent(this, ConectActivity::class.java)

            startActivity(intent)
        }
    }
}