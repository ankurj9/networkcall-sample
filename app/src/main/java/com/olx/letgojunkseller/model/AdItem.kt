package com.olx.letgojunkseller.model

import java.io.Serializable

data class AdItem(val id: Int, val imageUrl: String, val title: String, val category: String, val offers:List<Offer>):Serializable

data class Offer(val id:String, val username:String, val offer:String, val status:String):Serializable
