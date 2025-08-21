package com.ccc.listadecompracriolla.pydolarnetwork

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object DolarApi {
    /*private const val BASE_URL = "https://pydolarve.org/"
    caida Pydolar*/
    private const val BASE_URL = "https://ve.dolarapi.com/"
    private val retrofitInstance by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val retrofitService: ApiServicesPydolar by lazy {
        retrofitInstance.create(ApiServicesPydolar::class.java)
    }
}