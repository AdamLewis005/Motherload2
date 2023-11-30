package com.example.motherload2.Character

class Character (lon:Float,lat:Float) {
    private var name : String = ""
    private var lon : Float = lon
    private var lat : Float = lat
    private lateinit var voisins : String
    private lateinit var money : String
    private lateinit var pickaxe : String
    private lateinit var items : String

    companion object {
        @Volatile
        private var INSTANCE: Character? = null

        fun getInstance(lon:Float,lat:Float): Character {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Character(lon,lat).also { INSTANCE = it }
            }
        }
    }

    fun changename(name:String){
        this.name = name
    }
    fun changecood(lon :Float,lat :Float){
        this.lon = lon
        this.lat = lat
    }
    fun getlon():Float {
        return this.lon
    }
    fun getlat():Float{
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

    fun setitems(items : String){
        this.items = items
    }


}