package com.example.motherload2.View.Frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Offers
import com.example.motherload2.ConectView
import com.example.motherload2.R

class OffersFragment : Fragment() {// aucune idee de ce que je fait

    private val mListener: OnListFragmentInteractionListener = object :
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Offers?) {conectView.selectOffer(item)}
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Offers?)
    }

    private var loffres : List<Offers>? = null
    private var mAdapter:OffersRecycler? = null
    private lateinit var conectView: ConectView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        conectView = ViewModelProvider(requireActivity())[ConectView::class.java]
        conectView.offre.observe(viewLifecycleOwner, { offers ->
            mAdapter?.updateOffres(offers)
        })
        Log.d("g",conectView.offre.value.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers_list, container, false)
        // définir l'adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            if (loffres == null) loffres = ArrayList()
            if (mAdapter == null) mAdapter = OffersRecycler(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}