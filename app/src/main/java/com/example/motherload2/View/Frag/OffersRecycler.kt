package com.example.motherload2.View.Frag

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Offers
import com.example.motherload2.R

class OffersRecycler (private val mListener: OffersFragment.OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<OffersRecycler.ViewHolder>() {

    private val mValues = mutableListOf<Offers>()

    // Utilisez cette fonction pour mettre à jour la liste depuis le ViewModel
    fun updateOffres(newOffers: List<Offers>) {
        Log.d("teste",newOffers[0].offer_id)
        mValues.clear()
        mValues.addAll(newOffers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_note, parent, false)
        return ViewHolder(view)
    }

    // Cette méthode est utilisée par la reciclerView pour afficher un message dans un item de liste.
    // Il faut donc dans cette méthode mettre à jor les éléments de 'holder' de manière à ce qu'ils
    // affichent le message en position 'position'.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.midView.text = mValues[position].offer_id
        var textContent = mValues[position].quantite
        if (textContent.length > 50) textContent = textContent.substring(0, 47) + "..."
        holder.mquantityView.text = textContent
        holder.mView.setOnClickListener { mListener?.onListFragmentInteraction(holder.mItem) }
    }

    // Doit retourner le nombre d'élément à afficher dnas la liste
    override fun getItemCount(): Int {
        return mValues.size
    }

    // Notre viewHolder. Il s'agit de l'élément graphique correspondant à un item de liste.
    // Le notre contient 3 champs texte destinés à afficher la date, l'auteur et le début du contenu
    // du message
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val midView: TextView
        val mquantityView: TextView
        val mpriceView: TextView
        var mItem: Offers? = null
        override fun toString(): String {
            return super.toString() + " '" + midView.text + "'"
        }

        init {
            midView = mView.findViewById<View>(R.id.name) as TextView
            mquantityView = mView.findViewById<View>(R.id.quantaty) as TextView
            mpriceView = mView.findViewById<View>(R.id.price) as TextView
        }
    }

}