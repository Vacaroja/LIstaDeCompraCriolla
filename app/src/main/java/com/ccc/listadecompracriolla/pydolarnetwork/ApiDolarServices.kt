package com.ccc.listadecompracriolla.pydolarnetwork

import com.ccc.listadecompracriolla.entities.BcvEntity

/* class ApiDolarServices(
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
)*/

class ApiDolarServices(
    val fuente: String?,
    val nombre: String?,
    val compra: Float?,
    val venta: Float?,
    val promedio: Float?,
    val fechaActualizacion: String?,
)

fun BcvEntity.ToApiDolarServices(): ApiDolarServices? {
    return ApiDolarServices(
        fuente = this.fuente,
        nombre = this.nombre,
        compra = this.compra,
        venta = this.venta,
        promedio = this.promedio,
        fechaActualizacion = this.fechaActualizacion
    )
}