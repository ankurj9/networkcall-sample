package com.rygelouv.networkcalldslsample

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rygelouv.networkcalldslsample.interfaces.LetgoService
import com.rygelouv.networkcalldslsample.utils.ImageUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


object Repository {
    fun getAds(query: String) = networkCall<AdsResponse, List<AdItem>> {
        client = LetgoAPI.letgoService.getAds(query)
    }

    fun postAd(imagePath:String, title:String, category: String, price:String) = networkCall<PostingResponse, String> {
        val requestFilePath = File(imagePath)
        val requestBody = ImageUtils.getCompressedImageRequestBody(imagePath, 1024)
        val photoBody = MultipartBody.Part.createFormData("file", requestFilePath.name, requestBody!!)
        client = LetgoAPI.letgoService.postAd(photoBody,
                RequestBody.create(MediaType.parse("multipart/form-data"),title),
                RequestBody.create(MediaType.parse("multipart/form-data"),category),
                RequestBody.create(MediaType.parse("multipart/form-data"),price))
    }

}

data class AdItem(val id: Int, val imageUrl: String, val title: String, val category: String, val distance:String)

data class AdsResponse(val items: List<AdItem>): BaseApiResponse<AdItem>(), DataResponse<List<AdItem>> {
    override fun retrieveData(): List<AdItem> = items
}

data class PostingResponse(val status:String): BaseApiResponse<String>(), DataResponse<String> {
    override fun retrieveData(): String = status
}

abstract class BaseApiResponse<T> {
    var total_count: Int = 0
    var incomplete_results: Boolean = false
}



object LetgoAPI {
    var API_BASE_URL: String = "https://api.github.com/"
    val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }
    val httpClient: OkHttpClient.Builder
        get() {
            val client = OkHttpClient.Builder()
            client.addInterceptor(loggingInterceptor)
            return client
        }
    var builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    var retrofit = builder
            .client(httpClient.build())
            .build()


    var letgoService = retrofit.create<LetgoService>(LetgoService::class.java)
}