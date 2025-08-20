package com.ccc.listadecompracriolla.pydolarnetwork

import retrofit2.http.GET

interface ApiServicesPydolar {
    @GET("api/v1/dollar")
    suspend fun getData(): ApiDolarServices
}