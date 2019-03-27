package com.rygelouv.networkcalldslsample.interfaces

import com.rygelouv.networkcalldslsample.AdsResponse
import com.rygelouv.networkcalldslsample.PostingResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface LetgoService {
    @GET("myads.php")
    fun getAds(@Query("q") query: String): Deferred<Response<AdsResponse>>

    @Multipart
    @POST("postad.php")
    fun postAd(@Part image:MultipartBody.Part,
               @Part("title") title:RequestBody,
               @Part("category") category:RequestBody,
               @Part("price") price:RequestBody): Deferred<Response<PostingResponse>>
}