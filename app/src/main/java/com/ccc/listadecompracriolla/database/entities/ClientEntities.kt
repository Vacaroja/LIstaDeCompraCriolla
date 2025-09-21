package com.ccc.listadecompracriolla.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ccc.listadecompracriolla.Core.clases.ClientList
import com.ccc.listadecompracriolla.Core.clases.Product

@Entity(tableName = "clients")
data class ClientListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String,
    var presupuesto: String = ""
)

fun ClientListEntity.ToClientList(): ClientList{
    return ClientList(
        id = this.id,
        name = this.name,
        presupuesto = this.presupuesto
    )
}

// --- 2. Entidad para la tabla intermedia (Many-to-Many) ---
// Esta tabla clientes y productos
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
@Entity(tableName = "products", foreignKeys = [
    ForeignKey(
        entity = ClientListEntity::class,
        parentColumns = ["id"],
        childColumns = ["client"],
        onDelete = ForeignKey.CASCADE // Si se elimina un cliente, sus referencias en esta tabla también
    ),
], indices = [Index(value = ["client"])])
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String = "",
    var cant: Float = 1f,
    var price: Float = 0f,
    var checked: Boolean = false,
    var nota: String = "",
    var medida: String = "",
    var client: Int? = null
)
fun ProductEntity.toProduct(): Product{
    return Product(
        this.id,
        this.name,
        this.cant,
        this.price,
        this.checked,
        this.nota,
        this.medida,
        this.client
    )
}