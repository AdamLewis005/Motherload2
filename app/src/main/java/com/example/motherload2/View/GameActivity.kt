package com.example.motherload2.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConectView
import com.example.motherload2.R


class GameActivity : AppCompatActivity() {
    private lateinit var conectView: ConectView

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            conectView.deplacement()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes

        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameactivity)
        conectView = ViewModelProvider(this).get(ConectView::class.java)
        Log.d("same ?",conectView.getconnect().toString())

        val buttonShop : Button = findViewById(R.id.shop)
        buttonShop.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
/*
        val buttonInv : Button = findViewById(R.id.inv)
        buttonInv.setOnClickListener {
            val intent = Intent(this, InvActivity::class.java)

            startActivity(intent)
        }
*/


    }

    override fun onResume() {
        super.onResume()
        // L'activity repasse en avant plan : on relance la mise à jour des messages
        handler.post(updateRunnable)
    }
    override fun onPause() {
        // L'activity passe en arrière-plan : on coupe la mise à jour des messages :
        // Pour ce faire, on vire de la file d'attente le job qui était posté.
        handler.removeCallbacks(updateRunnable)

        super.onPause()
    }
}