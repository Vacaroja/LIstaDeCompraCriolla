package com.ccc.listadecompracriolla.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String,
    var presupuesto: Float = 0f
)

// --- 2. Entidad para la tabla intermedia (Many-to-Many) ---
// Esta tabla связывает clientes y productos
@Entity(
    tableName = "client_product_cross_ref",
    primaryKeys = ["clientId", "productId"], // La clave primaria es la combinación de ambos IDs
    foreignKeys = [
        // Clave foránea para el cliente
        ForeignKey(
            entity = ClientListEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE // Si se elimina un cliente, sus referencias en esta tabla también
        ),
        // Clave foránea para el producto
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE // Si se elimina un producto, sus referencias en esta tabla también
        )
    ]
)
data class ClientProductCrossRef(
    @ColumnInfo(index = true) val clientId: Int, // Agrega index = true para mejorar el rendimiento de las consultas
    @ColumnInfo(index = true) val productId: Int // Agrega index = true para mejorar el rendimiento de las consultas
)

// --- 3. Entidad para la tabla de Productos ---
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String = "",
    var cant: Float = 1f,
    var price: Float = 0f,
    var checked: Boolean = false,
    var nota: String = "",
    var medida: String = ""
)