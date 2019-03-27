package com.rygelouv.networkcalldslsample.interfaces

import com.rygelouv.networkcalldslsample.AdsResponse
import com.rygelouv.networkcalldslsample.PostingResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface LetgoService {
    @GET("search/repositories")
    fun getAds(@Query("q") query: String): Deferred<Response<AdsResponse>>

    @Multipart
    @POST("")
    fun postAd(@Part image:MultipartBody.Part,
               @Part("title") title:RequestBody,
               @Part("category") category:RequestBody,
               @Part("price") price:RequestBody): Deferred<Response<PostingResponse>>
}