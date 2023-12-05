package com.example.motherload2.View

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConectView
import com.example.motherload2.R
import com.example.motherload2.View.Frag.OffersFragment
import com.example.motherload2.View.Frag.OffredetailFrag

class ShopActivity : AppCompatActivity(){
    private lateinit var conectView: ConectView
    private var offersFragmaent : OffersFragment? = null
    private var offersdetailFragment: OffredetailFrag? = null

    // Pour faire une mise à jour toutes les 15 secondes : on va donner un job toutes les
    //15 secondes au thread principal. On récupère son handler ici...
    private val handler = Handler(Looper.getMainLooper())
    // ... Et voici le runnable qu'on va lui donner. Il déclenche la mise à jour et se programme
    // lui-même à nouveau pour être exécuté dnas 15 secondes.
    private val updateRunnable = object : Runnable {
        override fun run() {
            conectView.market()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes
        }
    }


    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopactivity)

        conectView = ViewModelProvider(this).get(ConectView::class.java)
        offersFragmaent = supportFragmentManager.findFragmentById(R.id.mainNoteFrag) as OffersFragment?
        offersdetailFragment = supportFragmentManager.findFragmentById(R.id.maindetailFrag)as OffredetailFrag?



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