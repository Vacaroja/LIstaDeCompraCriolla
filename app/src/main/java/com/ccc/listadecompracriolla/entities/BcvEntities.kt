package com.ccc.listadecompracriolla.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ccc.listadecompracriolla.pydolarnetwork.Bcv

// @Entity le dice a Room que esta clase representa una tabla en la base de datos
// 'tableName' es opcional, si no se especifica, usa el nombre de la clase
@Entity(tableName = "bcv_data")
data class BcvEntity(

    @PrimaryKey val id: Int = 1, // Usamos un ID fijo (1) porque solo habrá 1 entrada de BCV en esta tabla

    val change: Double,
    val color: String,
    val image: String,
    val lastUpdate: String, // No necesitamos @SerializedName aquí, ya que Room usa el nombre del campo Kotlin
    val percent: Double,
    val price: Float,
    val priceOld: Double,
    val symbol: String,
    val title: String
)

// Opcional: Función de extensión para convertir tu Bcv (de la API) a BcvEntity (para la DB)
fun Bcv.toBcvEntity(): BcvEntity {
    return BcvEntity(
        change = this.change,
        color = this.color,
        image = this.image,
        lastUpdate = this.lastUpdate, // Aquí mapeamos 'lastUpdate' de la API a la entidad
        percent = this.percent,
        price = this.price,
        priceOld = this.priceOld,
        symbol = this.symbol,
        title = this.title
    )
}

// Opcional: Función de extensión para convertir BcvEntity (de la DB) a Bcv (para usar en UI si es necesario)
fun BcvEntity.toBcv(): Bcv {
    return Bcv(
        change = this.change,
        color = this.color,
        image = this.image,
        lastUpdate = this.lastUpdate, // Aquí mapeamos 'lastUpdate' de la entidad a 'last_update' de la API data class
        percent = this.percent,
        price = this.price,
        priceOld = this.priceOld,
        symbol = this.symbol,
        title = this.title
    )
}