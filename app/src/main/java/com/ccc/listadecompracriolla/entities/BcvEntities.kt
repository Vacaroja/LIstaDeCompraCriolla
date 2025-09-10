package com.ccc.listadecompracriolla.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.listadecompracriolla.pydolarnetwork.ApiDolarServices
import kotlin.String


@Entity(tableName = "bcv_data")
data class BcvEntity(

    @PrimaryKey val id: Int = 1, // Usamos un ID fijo (1) porque solo habrá 1 entrada de BCV en esta tabla
    val usd: Double?,
    val eur: Double?,
    val date: String?
)

fun BcvEntity.toApiDolarServices(): ApiDolarServices{
    return ApiDolarServices(
        usd = this.usd,
        eur = this.eur,
        date = this.date
    )
}

fun ApiDolarServices.toBcvEntity(): BcvEntity{
    return BcvEntity(
        usd = this.usd,
        eur = this.eur,
        date = this.date
    )
}