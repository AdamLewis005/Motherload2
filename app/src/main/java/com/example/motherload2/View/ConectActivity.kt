package com.example.motherload2.View

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConectView
import com.example.motherload2.R

class ConectActivity : AppCompatActivity() {
    private lateinit var conectView: ConectView
    private lateinit var editTextLog : EditText
    private lateinit var editTextPass : EditText
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.connectactivity)

        conectView = ViewModelProvider(this).get(ConectView::class.java)
        Log.d("bob","fffffffffffffffffffffffffffffffffffffffffffffff")
        //editTextLog = findViewById(R.id.edit_log)
        //editTextPass = findViewById(R.id.edit_pass)

        val buttonConect : Button = findViewById(R.id.conect)
        buttonConect.setOnClickListener {
            // On récupère le contenu des EditText et on les utilise pour le nouveau message
            val log = "alewis"//editTextLog.text.toString()
            val pass = "LAt%24yc6@" //editTextPass.text.toString()
            conectView.conectWeb(log,pass)
            // Une fois le message ajouté, on peut quitter l'activity. Cela reviendra automatiquement
            // à l'actvity précédente, c'est à dire MainActivity.
            finish()
        }
    }
}