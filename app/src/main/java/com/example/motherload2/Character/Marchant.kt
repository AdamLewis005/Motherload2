package com.example.motherload2.Character

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Marchant {
    private val _offre = MutableLiveData<List<Offers>>()
    val offers: LiveData<List<Offers>> get() = _offre
    internal var items = listOf<Offers>()

    companion object {
        @Volatile
        private var INSTANCE: Marchant? = null

        fun getInstance(): Marchant {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Marchant().also { INSTANCE = it }
            }
        }
    }

    fun additem(offer: Offers){
        this.items += listOf(offer)

    }
    fun resetM(){
        this.items = listOf()
    }
    fun updatelive(){
        _offre.postValue(items)
        Log.d("up",offers.value.toString())
    }
}