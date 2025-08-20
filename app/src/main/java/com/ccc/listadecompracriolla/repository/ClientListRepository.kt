package com.ccc.listadecompracriolla.repository

import com.ccc.listadecompracriolla.dao.ClientDao
import com.ccc.listadecompracriolla.dao.ClientWithProducts
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ProductEntity
import com.ccc.listadecompracriolla.entities.ClientProductCrossRef
import kotlinx.coroutines.flow.map

@Singleton
class ClientRepository @Inject constructor(private val clientDao: ClientDao) {

    // --- Obtener clientes con sus productos ---
    fun getAllClientsWithProducts(): Flow<List<ClientWithProducts>> {
        return clientDao.getClientsWithProducts()
    }

    fun getClientWithProductsById(clientId: Int): Flow<ClientWithProducts?> {
        return clientDao.getClientWithProductsById(clientId)
    }

    // --- Operaciones básicas para Clientes ---
    suspend fun addClient(client: ClientListEntity): Long {
        return clientDao.insertClient(client)
    }

    suspend fun updateClient(client: ClientListEntity) {
        clientDao.updateClient(client)
    }

    suspend fun deleteClient(client: ClientListEntity) {
        clientDao.deleteClient(client)
    }

    // --- Operaciones básicas para Productos ---
    suspend fun addProduct(product: ProductEntity): Long { // Método general para añadir un producto
        return clientDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        clientDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        clientDao.deleteProduct(product)
    }

    // --- Métodos para gestionar la relación muchos a muchos ---
    suspend fun associateProductToClient(clientId: Int, productId: Int) {
        val crossRef = ClientProductCrossRef(clientId = clientId, productId = productId)
        clientDao.insertClientProductCrossRef(crossRef)
    }

    suspend fun disassociateProductFromClient(clientId: Int, productId: Int) {
        val crossRef = ClientProductCrossRef(clientId = clientId, productId = productId)
        clientDao.deleteClientProductCrossRef(crossRef)
    }

    // Opcional: Obtener todos los productos en general
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return clientDao.getAllProducts()
    }

    // Opcional: Obtener productos asociados a un cliente específico (si necesitas solo la lista de productos)
    @Deprecated("Usar getClientWithProductsById para la relación completa")
    fun getProductsForClient(clientId: Int): Flow<List<ProductEntity>> {
        // Esta consulta sería más compleja, involucrando JOINs a través de la tabla intermedia
        // Sin embargo, clientDao.getClientWithProductsById ya te da esto.
        // Esto es solo si realmente necesitas *solo* los productos sin la entidad cliente.
        return clientDao.getClientWithProductsById(clientId).map { clientWithProducts ->
            clientWithProducts?.products ?: emptyList()
        }
    }
}