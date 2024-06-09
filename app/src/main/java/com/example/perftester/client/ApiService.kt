package com.example.perftester.client

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("products/")
    suspend fun getProducts(): Response<List<Product>>

}