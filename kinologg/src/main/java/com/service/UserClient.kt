package com.example.kinologg.api.service

import retrofit2.Call
import com.example.kinologg.api.Login
import com.example.kinologg.api.model.Post
import com.example.kinologg.api.model.Token
import retrofit2.http.*




const val api_key = "df30dfe877c0565791c866b963e450b3"



interface UserClient {
    @POST("authentication/token/validate_with_login?api_key=df30dfe877c0565791c866b963e450b3")
    fun login(@Body login: Login): Call<Token>

    @GET("authentication/token/new?api_key=df30dfe877c0565791c866b963e450b3")
    fun getToken():Call<Token>

    @GET("posts")
    fun getPostList(): Call<List<Post>>
    @GET("posts/{id}")
    fun getPostById(@Path("id") id: Int): Call<Post>
}