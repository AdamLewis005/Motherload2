package com.example.motherload2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.motherload2.Character.Character
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Marchant
import com.example.motherload2.Character.Offers
import com.example.motherload2.Conect.Connection

class ConectView : ViewModel() {
    private val repository = Connection.getInstance()
    private val perso = Character.getInstance("1.9365061f","47.8430441f")
    val marchant = Marchant.getInstance()
    val offre: LiveData<List<Offers>> = repository.offers // Directement lié au LiveData du Repository

    private val _selectedoffers = MutableLiveData<Offers?>()
    val selectedMessage: LiveData<Offers?> get() = _selectedoffers

    fun conectWeb(login: String, password: String){
        repository.ConectWeb(login,password)
    }
    fun getconnect():Boolean{
        return repository.getConected()
    }
    fun changename(name:String){
        repository.changeName(name,perso)
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
    fun dig(){
        repository.dig(perso)
    }
    fun detailitem(id:String,item:Item){
        repository.item_detail(id,item)
    }
    fun market(){
        repository.marketlist(marchant)
    }
    fun selectOffer(offers: Offers?){
        _selectedoffers.postValue (offers)
        Log.d("MsgViewModel","Message sélectionné : "+offers?.offer_id)
    }

}