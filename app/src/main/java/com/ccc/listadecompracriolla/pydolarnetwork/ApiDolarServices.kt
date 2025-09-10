package com.ccc.listadecompracriolla.pydolarnetwork


/*class ApiDolarServices(
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
}*/

// 1. Data class para los valores de las monedas (current y previous)

data class ApiDolarServices(
    val usd: Double?,
    val eur: Double?,
    val date: String?
)
data class ChangePercentage(
    val usd: Double?,
    val eur: Double?
)
data class DolarBcvResponse(
    val current: ApiDolarServices,
    val previous: ApiDolarServices,
    val changePercentage: ChangePercentage
)