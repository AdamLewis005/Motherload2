package com.example.motherload2.Character

class Character (lon:String,lat:String) {
    private var name : String = ""
    private var lon : String = lon
    private var lat : String = lat
    private lateinit var voisins : String
    private lateinit var money : String
    private lateinit var pickaxe : String
    private lateinit var items : String

    companion object {
        @Volatile
        private var INSTANCE: Character? = null

        fun getInstance(lon:String,lat:String): Character {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Character(lon,lat).also { INSTANCE = it }
            }
        }
    }

    fun changename(name:String){
        this.name = name
    }
    fun changecood(lon :String,lat :String){
        this.lon = lon
        this.lat = lat
    }
    fun getlon():String {
        return this.lon
    }
    fun getlat():String{
        return this.lat
    }

    fun getvoisin():String{
        return this.voisins
    }

    fun setvoisins(voisins : String){
        this.voisins = voisins
    }

    fun setmoney(money : String){
        this.money = money
    }

    fun setpick(pick :String){
        this.pickaxe = pick
    }

    fun additem(item : String){
        this.items += item
    }

    fun setitems (items : String){
        this.items = items
    }





}