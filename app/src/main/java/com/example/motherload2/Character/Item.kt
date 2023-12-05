package com.example.motherload2.Character

class Item (id:String) {
    private val id = id
    internal var nom :String =""
    internal var  type :String =""
    internal var rarete :String =""
    private var image :String=""
    internal var decFr :String =""
    private var decEn :String=""

    companion object {
        @Volatile
        private var INSTANCE: Item? = null

        fun getInstance(id:String): Item {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Item(id).also { INSTANCE = it }
            }
        }
    }
    fun createitem(nom:String,type:String,rarete: String,image: String,decEn: String,decFr: String){
        this.nom= nom
        this.type=type
        this.decEn = decEn
        this.rarete = rarete
        this.image = image
        this.decFr = decFr
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