package com.example.motherload2.Character

class Marchant {
    private var items = listOf<Offers>()

    fun additem(offer: Offers){
        this.items += listOf(offer)
    }
    fun resetM(){
        this.items = listOf()
    }
}