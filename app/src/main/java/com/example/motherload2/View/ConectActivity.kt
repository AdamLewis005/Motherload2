package com.example.motherload2.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.Character.Item
import com.example.motherload2.ConectView
import com.example.motherload2.R

class ConectActivity : AppCompatActivity() {
    private lateinit var conectView: ConectView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.connectactivity)

        conectView = ViewModelProvider(this).get(ConectView::class.java)
        //editTextLog = findViewById(R.id.edit_log)
        //editTextPass = findViewById(R.id.edit_pass)





        val buttonConect : Button = findViewById(R.id.conect)
        buttonConect.setOnClickListener {
            // on recupere le login et le mot de passe
            val log = "alewis"//editTextLog.text.toString()
            val pass = "LAt%24yc6@" //editTextPass.text.toString()
            conectView.conectWeb(log,pass)
            Log.d("connected ?",conectView.getconnect().toString())
            if (conectView.getconnect()) {
                // button ferme avant la fin de la verif donc faux apuyer 2 fois pour linstant faux faire un delay ou att que la fonction finis
                val intent = Intent(this, GameActivity::class.java)

                startActivity(intent)
                // Une fois connecter, on peut quitter l'activity. Cela reviendra automatiquement
                // à l'actvity précédente, c'est à dire MainActivity.
                finish()
            }
            finish()
        }




        val buttonname : Button = findViewById(R.id.changename)
        buttonname.setOnClickListener {
            // on recupere le nom que lon veut changer
            val name = "alewis"//editTextName.text.toString()
            conectView.changename(name)
            // Une fois le nom changer, on peut quitter l'activity. Cela reviendra automatiquement
            // à l'actvity précédente, c'est à dire MainActivity.
            finish()
        }

        val buttoncoords : Button = findViewById(R.id.coords)
        buttoncoords.setOnClickListener {
            conectView.deplacement()

        }
        val buttonStatus : Button = findViewById(R.id.status)
        buttonStatus.setOnClickListener {
            conectView.statusplayer()
        }
        val buttonreinit : Button = findViewById(R.id.reinit)
        buttonreinit.setOnClickListener {
            conectView.reinitplayer()
        }

        val buttondig : Button = findViewById(R.id.dig)
        buttondig.setOnClickListener {
            conectView.dig()
        }

        val buttonitem : Button = findViewById(R.id.item)
        buttonitem.setOnClickListener{
            val id = "5"// get texte id
            val item = Item.getInstance(id)
            conectView.detailitem(id,item)
        }
        val buttonmarket : Button = findViewById(R.id.market)
        buttonmarket.setOnClickListener {
            conectView.market()
        }





    }
}