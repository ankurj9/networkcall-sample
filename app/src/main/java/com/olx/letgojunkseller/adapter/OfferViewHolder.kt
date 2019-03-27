package com.olx.letgojunkseller.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.olx.letgojunkseller.model.Offer
import com.olx.letgojunkseller.interfaces.OfferClickListener
import kotlinx.android.synthetic.main.item_offer.view.*

class OfferViewHolder(view: View,val offerClickListener: OfferClickListener) : RecyclerView.ViewHolder(view) {
    fun bindView(item: Offer) {

        itemView.userName.text = item.username
        itemView.priceValue.text = item.offer
        when(item.status) {
            "pending" -> {
                itemView.acceptCTA.visibility = View.VISIBLE
                itemView.rejectCTA.visibility = View.VISIBLE
                itemView.acceptLabel.visibility = View.GONE
                itemView.rejectLabel.visibility = View.GONE

            }
            "accepted" -> {
                itemView.acceptCTA.visibility = View.GONE
                itemView.rejectCTA.visibility = View.GONE
                itemView.acceptLabel.visibility = View.VISIBLE
                itemView.rejectLabel.visibility = View.GONE
            }
            "decline" -> {
                itemView.acceptCTA.visibility = View.GONE
                itemView.rejectCTA.visibility = View.GONE
                itemView.acceptLabel.visibility = View.GONE
                itemView.rejectLabel.visibility = View.VISIBLE
            }
        }

        itemView.acceptCTA.setOnClickListener {
            offerClickListener.onOfferClicked(item.id, "accepted")
        }
        itemView.rejectCTA.setOnClickListener {
            offerClickListener.onOfferClicked(item.id, "decline")
        }
    }
}