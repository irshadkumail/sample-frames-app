package com.example.perftester.data

import com.example.perftester.client.ApiService
import com.example.perftester.client.Product
import retrofit2.Response
import javax.inject.Inject

interface ProductRepository {

    suspend fun getProducts(): Response<List<Product>>
}

class ProductRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    ProductRepository {

    override suspend fun getProducts(): Response<List<Product>> {
        return apiService.getProducts()
    }

}