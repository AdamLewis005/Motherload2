package com.example.motherload2.Conect


import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.motherload2.App
import com.example.motherload2.Character.Character
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


    fun changeName(name : String) {
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
        on envoie au serveur les nouvels coordonners
         */



        if (!this.conected) {
            // on verifie qu on soit bien connecter au serveur et quon ai recuperer la sessio et la signature
            Log.e(TAG,"Not Connected")
            return
        }


        val encodeses = URLEncoder.encode(this.session, "UTF-8")
        val encodesig = URLEncoder.encode(this.signature, "UTF-8")
        val encodeco = URLEncoder.encode("0.0F", "UTF-8")


        val url =
            BASE_URL + "/deplace.php?session=$encodeses&signature=$encodesig" +
                    "&lon=${character.getlon()}&lat=${character.getlat()}"

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
                            //character.changecood(pose)
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

}