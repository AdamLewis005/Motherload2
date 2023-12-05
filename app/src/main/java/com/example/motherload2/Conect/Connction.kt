package com.example.motherload2.Conect


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.motherload2.App
import com.example.motherload2.Character.Character
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Marchant
import com.example.motherload2.Character.Offers
import org.w3c.dom.Document
import java.net.URLEncoder
import java.security.MessageDigest
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class Connection private constructor() {
    private val TAG = "Connection"
    private val BASE_URL = " https://test.vautard.fr/creuse_srv/"
    private lateinit var session : String
    private lateinit var signature : String
    private var conected : Boolean = false
    private val _offers = MutableLiveData<List<Offers>>()
    val offers: LiveData<List<Offers>> get() = _offers
    private val oListe = ArrayList<Offers>()

    companion object {
        @Volatile
        private var INSTANCE: Connection? = null

        fun getInstance(): Connection {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Connection().also { INSTANCE = it }
            }
        }
    }

    fun ConectWeb(Login: String, Password: String) {
    /*
        fonction utiliser pour se connecter au serveur en donnent en paramettre lindentifiant et le mot de passe
    */
        val encodepass = hash(Password)
        val encodedLog = URLEncoder.encode(Login, "UTF-8")

        val url = BASE_URL + "/connexion.php?login=$encodedLog&passwd=$encodepass"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Connection: Log with succes")
                            val sessionNode = doc.getElementsByTagName("SESSION").item(0)
                            val signatureNode = doc.getElementsByTagName("SIGNATURE").item(0)
                            this.session = sessionNode.textContent.trim()
                            this.signature = signatureNode.textContent.trim()
                            this.conected = true
                            Log.d("signature",this.signature)
                            Log.d("session",this.session)



                        } else {
                            Log.e(TAG, "Connection: Erreur - $status")
                            // popup with status Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Connection error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun hash(str : String ): String {
        /*
        permet le hashage en SHA256 du mot de passe qui est ensuite envoyer au serveur
         */
        val bytes = str.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
    fun getConected() : Boolean {
        return this.conected
    }

    fun getSession() : String{
        return this.session
    }
    fun getSignature() : String{
        return this.signature
    }


    fun changeName(name : String,perso:Character) {
        /*
        fonction pour communiquer avec le serveur pour changer le psodo
         */

        if (!this.conected) {// on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodename = URLEncoder.encode(name, "UTF-8")
        val url = BASE_URL + "/changenom.php?session=$encodeses&signature=$encodesig&nom=$encodename"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Changename: Name Changed")
                            perso.changename(name)



                        } else {
                            Log.e(TAG, "Changename: Erreur - $status")
                            // popup with Changename Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Changename error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun deplacement(character: Character){
        /*
        on envoie au serveur les nouvels coordonners faudra appeler cette fonction tout les x temps
         */



        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodelat = URLEncoder.encode(character.getlat(), "UTF-8")
        val encodelon = URLEncoder.encode(character.getlon(),"UTF-8")


        val url =
            BASE_URL + "/deplace.php?session=$encodeses&signature=$encodesig" +
                    "&lon=$encodelon&lat=$encodelat"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Deplacement: deplacer")
                            val voisinsNode = doc.getElementsByTagName("VOISINS").item(0)
                            character.setvoisins(voisinsNode.textContent.trim())
                            Log.d("voisins",character.getvoisin())

                        } else {
                            Log.e(TAG, "Deplacement: Erreur - $status")
                            // popup with Deplacement Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Deplacement error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

    fun statusplayer(character: Character) {
        /*
        demande au serveur de recuperer des information sur notre joueur
        inventaire/argent/position/niveau de pick
         */

        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url =
            BASE_URL + "/status_joueur.php?session=$encodeses&signature=$encodesig"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Status_joueur: Status obtenu")
                                                                                            // PAS FINI
                            val moneyNode = doc.getElementsByTagName("MONEY").item(0)
                            val money = moneyNode.textContent.trim()
                            Log.d("items",money)
                            character.setmoney(money)
                            val pickNode = doc.getElementsByTagName("PICKAXE").item(0)
                            val pick = pickNode.textContent.trim()
                            character.setpick(pick)
                            Log.d("pick",pick)
                            val positionNode = doc.getElementsByTagName("POSITION").item(0)
                            val pose = positionNode.textContent.trim()
                            Log.d("pose",pose)
                            val itemsNode = doc.getElementsByTagName("ITEMS").item(0)
                            val items = itemsNode.textContent.trim()
                            Log.d("items",items)
                            character.setitems(items)




                        } else {
                            Log.e(TAG, "status_joueur: Erreur - $status")
                            // popup with Deplacement Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "status_joueur error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)

    }

    fun reinit_player() {
        /*
        reset le joueur a 0 au niveau du serveur faux metre signal de warning pour eviter les miss click
         */

        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG, "Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url =
            BASE_URL + "/reinit_joueur.php?session=$encodeses&signature=$encodesig"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    Log.d(TAG, "reset succesful")
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "reinit_joueur error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }

    fun dig(character: Character){

        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodelat = URLEncoder.encode(character.getlat(), "UTF-8")
        val encodelon = URLEncoder.encode(character.getlon(),"UTF-8")


        val url =
            BASE_URL + "/creuse.php?session=$encodeses&signature=$encodesig" +
                    "&lon=$encodelon&lat=$encodelat"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Creuser: succesful dig")
                            val voisinsNode = doc.getElementsByTagName("VOISINS").item(0)
                            character.setvoisins(voisinsNode.textContent.trim())
                            Log.d("voisins",character.getvoisin())
                            val dethNode = doc.getElementsByTagName("DEPTH").item(0)
                            val deth = dethNode.textContent.trim()
                            Log.d("Deth",deth)
                            val itemNode = doc.getElementsByTagName("ITEM_ID").item(0)

                            if (itemNode != null) {
                                val item = itemNode.textContent.trim()
                                character.additem(item)
                                Log.d("item got", item)
                            }

                        } else {
                            Log.e(TAG, "Creuser: Erreur - $status")
                            // popup with creuser Error avec le satus attacher
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "Creuser error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)

    }

    fun item_detail(item_id : String,item: Item) {
        /*
        demande le resumer dun object au serveur
         */

        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG, "Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeitem = URLEncoder.encode(item_id,"UTF-8")

        val url =
            BASE_URL + "/item_detail.php?session=$encodeses&signature=$encodesig&item_id=$encodeitem"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "Detail: detail obtenu")
                            val nomNode = doc.getElementsByTagName("NOM").item(0)
                            val nom = nomNode.textContent.trim()
                            Log.d("detail_item",nom)
                            item.setnom(nom)
                            val typeNode = doc.getElementsByTagName("TYPE").item(0)
                            val type = typeNode.textContent.trim()
                            Log.d("detail_item",type)
                            item.settype(type)
                            val rareteNode = doc.getElementsByTagName("RARETE").item(0)
                            val rarete = rareteNode.textContent.trim()
                            Log.d("detail_item",rarete)
                            item.setrarete(rarete)
                            val imageNode = doc.getElementsByTagName("IMAGE").item(0)
                            val image = imageNode.textContent.trim()
                            Log.d("detail_item",image)
                            item.setimage(image)
                            val decFrNode = doc.getElementsByTagName("DESC_FR").item(0)
                            val decFr = decFrNode.textContent.trim()
                            Log.d("detail_item",decFr)
                            item.setdecFr(decFr)
                            val decEnNode = doc.getElementsByTagName("DESC_EN").item(0)
                            val decEn = decEnNode.textContent.trim()
                            Log.d("detail_item",decEn)
                            item.setdecEn(decEn)




                        } else {
                        Log.e(TAG, "item detail: Erreur - $status")
                        // popup with detail Error avec le satus attacher
                        }
                    }
                }catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }

            },
            { error ->
                Log.d(TAG, "item detail error")
                error.printStackTrace()
            })

        App.instance.requestQueue?.add(stringRequest)
    }


    fun marketlist(marchant: Marchant) {
        /*
        fonction pour communiquer avec le serveur pour obtenir les offre
         */

        if (!this.conected) {// on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }

        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")

        val url = BASE_URL + "/market_list.php?session=$encodeses&signature=$encodesig"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // la réponse retournée par le WS si succès
                try {
                    val docBF: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
                    val docBuilder: DocumentBuilder = docBF.newDocumentBuilder()
                    val doc: Document = docBuilder.parse(response.byteInputStream())

                    // On vérifie le status
                    val statusNode = doc.getElementsByTagName("STATUS").item(0)
                    if (statusNode != null) {
                        val status = statusNode.textContent.trim()

                        if (status == "OK") {
                            Log.d(TAG, "market_list: market_list obtained")
                            val offersNode=doc.getElementsByTagName("OFFERS")
                            var i = 0
                            marchant.resetM()
                            while (i < offersNode.length){
                                val itemNode = doc.getElementsByTagName("item$i").item(0)

                                val offer_idNode = itemNode.firstChild
                                val offer_id = offer_idNode.textContent.trim()
                                Log.d("offres",offer_id)
                                val itemidNode = offer_idNode.nextSibling
                                val itemid = itemidNode.textContent.trim()
                                Log.d("item",itemid)
                                val quantityNode = itemidNode.nextSibling
                                val quantity = quantityNode.textContent.trim()
                                Log.d("item",quantity)
                                val prixNode = quantityNode.nextSibling
                                val prix = prixNode.textContent.trim()
                                Log.d("item",prix)
                                val offre = Offers(offer_id,itemid,quantity,prix)
                                marchant.additem(offre)
                                oListe.add(offre)
                                Log.d("marchant","succes")
                                i += 1

                            }
                            _offers.postValue(oListe)
                            Log.d("add", _offers.value?.get(0).toString() )




                        } else {
                            Log.e(TAG, "market_list: Erreur - $status")
                            // popup with market_list Error
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Erreur lors de la lecture de la réponse XML", e)
                }
            },
            { error ->
                Log.d(TAG, "market_list error")
                error.printStackTrace()
            })
        // ligne importante a ne pas oublier
        App.instance.requestQueue?.add(stringRequest)
    }

}