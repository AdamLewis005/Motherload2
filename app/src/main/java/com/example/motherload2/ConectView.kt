package com.example.motherload2

import androidx.lifecycle.ViewModel
import com.example.motherload2.Character.Character
import com.example.motherload2.Conect.Connection

class ConectView : ViewModel() {
    private val repository = Connection.getInstance()
    private val perso = Character.getInstance(0.0F,0.0F)

    fun conectWeb(login: String, password: String){
        repository.ConectWeb(login,password)
    }
    fun changename(name:String){
        repository.changeName(name)
    }

    fun deplacement(){
        repository.deplacement(perso)
    }
    fun statusplayer(){
        repository.statusplayer(perso)
    }
    fun reinitplayer(){
        repository.reinit_player()
    }
}