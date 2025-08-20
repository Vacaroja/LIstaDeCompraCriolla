package com.ccc.listadecompracriolla.pydolarnetwork

import com.google.gson.annotations.SerializedName

data class ApiDolarServices(
    val datetime: Datetime,
    val monitors: Monitors
)

// Datos de fecha y hora
data class Datetime(
    val date: String,
    val time: String
)

// Contenedor para todos los monitores
data class Monitors(
    val bcv: Bcv,
)

data class Bcv(
    val change: Double,
    val color: String,
    val image: String,
    @SerializedName("last_update") val lastUpdate: String,
    val percent: Double,
    val price: Float,
    @SerializedName("price_old") val priceOld: Double,
    val symbol: String,
    val title: String
)

