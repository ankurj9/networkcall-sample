package com.rygelouv.networkcalldslsample

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Query


object Repository {
    fun getRepos(query: String) = networkCall<ReposResponse, List<Repo>> {
        client = GithubAPI.githubService.getRepos(query)
    }
}

data class Repo(val id: Int, val name: String, val full_name: String, val description: String, val git_url:String)

data class ReposResponse(val items: List<Repo>): BaseApiResponse<Repo>(), DataResponse<List<Repo>> {
    override fun retrieveData(): List<Repo> = items
}

abstract class BaseApiResponse<T> {
    var total_count: Int = 0
    var incomplete_results: Boolean = false
}

object GithubAPI {
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


    var githubService = retrofit.create<GithubService>(GithubService::class.java)

    interface GithubService {
        @GET("search/repositories")
        fun getRepos(@Query("q") query: String): Deferred<Response<ReposResponse>>
    }
}