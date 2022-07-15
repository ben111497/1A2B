package com.example.wordle_1A2B.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface AttractionsClassInterface {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("/open-api/{language}/Attractions/All")
    fun get(@Path("language") language: String, @Query("page") limit: Int): Call<AttractionsClassRes>

    companion object {
        private const val URL = "https://www.travel.taipei"
        private val okHttpClient = OkHttpClient()
        private val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AttractionsClassInterface::class.java)
    }
}

data class AttractionsClassRes(val total: Int?, val data: ArrayList<Data>?) {
    class Data(val id: Int?, val name: String?, val name_zh: String?,
               val open_status: Int?, val introduction: String?, val open_time: String?,
               val zipcode: String?, val distric: String?, val address: String?,
               val tel: String?, val fax: String?, val email: String?,
               val months: String?, val nlat: Double?, val elong: Double?,
               val official_site: String?, val facebook: String?, val ticket: String?,
               val remind: String?, val staytime: String?, val modified: String?,
               val url: String?, val category: ArrayList<Category>?, val target: ArrayList<Target>?,
               val service: ArrayList<Service>?, val friendly: ArrayList<Friendly>?, val images: ArrayList<Images>?,
               val links: ArrayList<Links>?)

    class Category(val id: Int?, val name: String?)
    class Target(val id: Int?, val name: String?)
    class Service(val id: Int?, val name: String?)
    class Friendly(val id: Int?, val name: String?)
    class Images(val src: String?, val subject: String?, val ext: String?)
    class Links(val src: String?, val subject: String?)
}