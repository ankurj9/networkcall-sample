package com.rygelouv.networkcalldslsample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rygelouv.model.Offer
import com.rygelouv.networkcalldslsample.R
import com.rygelouv.networkcalldslsample.interfaces.OfferClickListener

open class OfferListAdapter(var list: ArrayList<Offer>, val offerClickListener: OfferClickListener) : RecyclerView.Adapter<OfferViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(view, offerClickListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun replace(data: List<Offer>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }


}