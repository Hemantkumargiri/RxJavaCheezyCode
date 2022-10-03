package com.example.rxjava

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ProductService {

    @GET("/products")
    fun getProducts(): Observable<List<ProductItem>>
}