package com.ccc.listadecompracriolla.pydolarnetwork

import retrofit2.http.GET

interface ApiServicesPydolar {
    @GET("v1/dolares/oficial")
    suspend fun getData(): ApiDolarServices
}