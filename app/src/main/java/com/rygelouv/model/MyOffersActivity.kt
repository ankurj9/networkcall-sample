package com.rygelouv.model

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rygelouv.networkcalldslsample.R
import com.rygelouv.networkcalldslsample.Repository
import com.rygelouv.networkcalldslsample.Resource
import com.rygelouv.networkcalldslsample.adapter.OfferListAdapter
import com.rygelouv.networkcalldslsample.interfaces.OfferClickListener
import kotlinx.android.synthetic.main.activity_offer.*
import kotlinx.android.synthetic.main.repo_item.*

class MyOffersActivity:AppCompatActivity(), OfferClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer)
        supportActionBar?.title = "Offers"
        val adData = intent.getSerializableExtra("ad_data") as AdItem
        setAdData(adData)
    }

    private fun setAdData(item:AdItem) {
        if(item.imageUrl!=null) {
            Glide.with(this).
                    load(item.imageUrl).
                    apply(RequestOptions().centerCrop()).
                    into(adImage)
        }
        adTitle.text = item.title
        categoryName.text = item.category
        offerCount.visibility = View.GONE
        myOfferCTA.visibility = View.GONE

        val adapter = OfferListAdapter(ArrayList(item.offers), this)
        offerList.layoutManager = LinearLayoutManager(this)
        offerList.adapter = adapter
    }

    override fun onOfferClicked(id: String, status:String) {
        respondToOffer(id, status)
    }

    fun respondToOffer(id:String, status:String) {
        val dialog = AlertDialog.Builder(this).setMessage("Please wait...").create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        Repository.respondToOffer(id,status).observe(this, Observer {
            when(it.status) {
                Resource.LOADING -> {
                    dialog.show()
                }
                Resource.SUCCESS -> {
                    dialog.dismiss()
                    val finalStatus = if(status =="decline") "Rejected" else "Accepted"
                    Toast.makeText(this, "Ad $finalStatus Successfully", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        })
    }
}