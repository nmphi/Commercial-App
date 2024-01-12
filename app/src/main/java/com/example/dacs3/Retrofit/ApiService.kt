package com.example.dacs3.Retrofit

import com.example.dacs3.model.CheckOut
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "http://10.0.2.2:8000/api/"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
interface ApiService {
    @GET("shop")
    fun getProduct():
            Call<String>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ):
            Call<String>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ):
            Call<String>

    @GET("user/{id}")
    fun getHistory(
        @Path("id") id: String
    ): Call<String>

    @POST("checkout")
    fun checkout(
        @Body data:CheckOut
    ): Call<String>
}

object Api {
    val retrofitService : ApiService by lazy {
        retrofit.create(ApiService::class.java) }
}