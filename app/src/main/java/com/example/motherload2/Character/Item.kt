package com.example.motherload2.Character

class Item (id:String) {
    private val id = id
    private lateinit var nom :String
    private lateinit var  type :String
    private lateinit var rarete :String
    private lateinit var image :String
    private lateinit var decFr :String
    private lateinit var decEn :String

    companion object {
        @Volatile
        private var INSTANCE: Item? = null

        fun getInstance(id:String): Item {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Item(id).also { INSTANCE = it }
            }
        }
    }
    fun setnom(nom:String){
        this.nom = nom
    }
    fun settype(type:String){
        this.type = type
    }
    fun setrarete(rarete:String){
        this.rarete=rarete
    }
    fun setimage(image:String){
        this.image = image
    }
    fun setdecFr(decFr:String){
        this.decFr = decFr
    }
    fun setdecEn(decEn:String){
        this.decEn = decEn
    }
}