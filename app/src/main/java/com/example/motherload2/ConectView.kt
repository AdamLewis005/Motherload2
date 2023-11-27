package com.example.motherload2

import androidx.lifecycle.ViewModel
import com.example.motherload2.Conect.Connection

class ConectView : ViewModel() {
    private val repository = Connection.getInstance()

    fun conectWeb(login: String, password: String){
        repository.ConectWeb(login,password)
    }
}