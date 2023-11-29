package com.example.motherload2.Character

class Character (lon: Float,lat: Float) {
    private var lon : Float = lon
    private var lat : Float = lat
    private lateinit var voisins : String
    private var money : Int = 0
    private lateinit var pickaxe : String
    private lateinit var items : IntArray

    fun changecood(lon : Float,lat : Float){
        this.lon = lon
        this.lat = lat
    }
    fun getlon(): Float {
        return this.lon
    }
    fun getlat(): Float{
        return this.lat
    }

    fun setvoisins(voisins : String){
        this.voisins = voisins
    }

    fun setmoney(money : Int){
        this.money = money
    }

    fun setpick(pick :String){
        this.pickaxe = pick
    }


}