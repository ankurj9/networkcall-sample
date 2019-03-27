package com.olx.letgojunkseller.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.olx.letgojunkseller.model.AdItem
import com.olx.letgojunkseller.model.MyOffersActivity
import kotlinx.android.synthetic.main.repo_item.view.*

class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(item: AdItem) {

        if(item.imageUrl!=null) {
            Glide.with(itemView.context).
                    load(item.imageUrl).
                    apply(RequestOptions().centerCrop()).
                    into(itemView.adImage)
        }
        itemView.adTitle.text = item.title
        itemView.categoryName.text = item.category
        itemView.offerCount.text = "${item.offers.size} offers received"
        itemView.myOfferCTA.setOnClickListener {
            val intent = Intent(itemView.context, MyOffersActivity::class.java)
            intent.putExtra("ad_data", item)
            itemView.context.startActivity(intent)
        }
    }
}