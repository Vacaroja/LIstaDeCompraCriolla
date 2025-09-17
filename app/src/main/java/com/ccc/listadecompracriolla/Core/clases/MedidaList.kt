package com.ccc.listadecompracriolla.Core.clases

import com.ccc.listadecompracriolla.entities.MedidaEntities

data class MedidaList(
    val idMedida:Int? = null,
    val medida: String = ""
)

fun MedidaList.toMedidaEntities(): MedidaEntities{
    return MedidaEntities(idMedida = this.idMedida,medida = this.medida)
}

fun MedidaEntities.toMedidaList(): MedidaList{
    return MedidaList(idMedida = this.idMedida,medida = this.medida)
}