package com.ccc.listadecompracriolla.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.listadecompracriolla.pydolarnetwork.ApiDolarServices
import kotlin.String


@Entity(tableName = "bcv_data")
data class BcvEntity(

    @PrimaryKey val id: Int = 1, // Usamos un ID fijo (1) porque solo habrá 1 entrada de BCV en esta tabla
    val fuente : String?,
    val nombre:String?,
    val compra: Float?,
    val venta: Float?,
    val promedio: Float?,
    val fechaActualizacion : String?

)

fun BcvEntity.toApiDolarServices(): ApiDolarServices{
    return ApiDolarServices(
        fuente =this.fuente,
        nombre = this.nombre,
        compra =this.compra,
         venta =this.venta,
        promedio =this.promedio,
        fechaActualizacion =this.fechaActualizacion
    )
}

fun ApiDolarServices.toBcvEntity(): BcvEntity{
    return BcvEntity(
        fuente =this.fuente,
        nombre = this.nombre,
        compra =this.compra,
        venta =this.venta,
        promedio =this.promedio,
        fechaActualizacion =this.fechaActualizacion
    )
}