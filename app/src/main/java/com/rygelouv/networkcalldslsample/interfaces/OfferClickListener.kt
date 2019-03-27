package com.rygelouv.networkcalldslsample.interfaces

import com.rygelouv.model.Offer

interface OfferClickListener {
    fun onOfferClicked(id: String, status:String)
}