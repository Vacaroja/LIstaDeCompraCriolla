package com.ccc.listadecompracriolla.dao

import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ClientProductCrossRef
import com.ccc.listadecompracriolla.entities.ProductEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// 1. Cliente con sus productos (lista de productos para un cliente)
data class ClientWithProducts(
    @Embedded val client: ClientListEntity,
    @Relation(
        parentColumn = "id", // Columna en ClientListEntity
        entityColumn = "id", // Columna en ProductEntity
        associateBy = Junction( // Clave para la relación muchos a muchos
            ClientProductCrossRef::class,
            parentColumn = "clientId", // Columna de ClientProductCrossRef que apunta a ClientListEntity
            entityColumn = "productId" // Columna de ClientProductCrossRef que apunta a ProductEntity
        )
    )
    val products: List<ProductEntity>
)

@Dao
interface ClientDao{

    // --- Operaciones básicas para ClientListEntity ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientListEntity): Long // Retorna el ID del cliente insertado

    @Update
    suspend fun updateClient(client: ClientListEntity)

    @Delete
    suspend fun deleteClient(client: ClientListEntity)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<ClientListEntity>>

    // --- Operaciones básicas para ProductEntity ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long // Retorna el ID del producto insertado

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    // --- Operaciones para la tabla intermedia (ClientProductCrossRef) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClientProductCrossRef(crossRef: ClientProductCrossRef)

    @Delete
    suspend fun deleteClientProductCrossRef(crossRef: ClientProductCrossRef)

    // --- Transacciones con Relaciones (Muchas a Muchas) ---

    // Obtener todos los clientes con sus productos asociados
    @Transaction
    @Query("SELECT * FROM clients")
    fun getClientsWithProducts(): Flow<List<ClientWithProducts>>

    // Obtener un cliente específico con sus productos

    @Query("SELECT * FROM clients WHERE id = :clientId")
    fun getClientById(clientId: Int?): ClientListEntity

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: Int?): ProductEntity


}