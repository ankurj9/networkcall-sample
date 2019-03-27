package com.rygelouv.networkcalldslsample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rygelouv.model.AdItem
import com.rygelouv.networkcalldslsample.R

open class AdListAdapter(var list: ArrayList<AdItem>) : RecyclerView.Adapter<AdViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.repo_item, parent, false)
        return AdViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    fun replace(data: List<AdItem>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }


}