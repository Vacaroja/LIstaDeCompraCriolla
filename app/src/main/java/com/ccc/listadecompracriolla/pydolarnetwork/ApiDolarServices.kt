package com.ccc.listadecompracriolla.pydolarnetwork

import com.ccc.listadecompracriolla.entities.BcvEntity


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