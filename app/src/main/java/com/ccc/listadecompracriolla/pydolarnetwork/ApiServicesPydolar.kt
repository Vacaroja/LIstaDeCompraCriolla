package com.ccc.listadecompracriolla.pydolarnetwork

import retrofit2.http.GET

interface ApiServicesPydolar {
    @GET("public/exchange-rate")
    suspend fun getData(): DolarBcvResponse
}