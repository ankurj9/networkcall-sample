package com.rygelouv.networkcalldslsample.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rygelouv.networkcalldslsample.AdItem
import kotlinx.android.synthetic.main.repo_item.view.*

class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(item: AdItem) {
        itemView.repoName.text = "${item.full_name} -> ${item.git_url}"
    }
}