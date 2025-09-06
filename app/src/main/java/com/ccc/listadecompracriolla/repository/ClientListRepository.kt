package com.ccc.listadecompracriolla.repository

import com.ccc.listadecompracriolla.Core.clases.ClientList
import com.ccc.listadecompracriolla.dao.ClientDao
import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ClientProductCrossRef
import com.ccc.listadecompracriolla.entities.ProductEntity
import com.ccc.listadecompracriolla.entities.ToClientList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepository @Inject constructor(private val clientDao: ClientDao) {

    // Opcional: Obtener todos los productos en general
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return clientDao.getAllProducts()
    }
    // Opcional: Obtener todos los productos en general
    fun getAllClients(): Flow<List<ClientListEntity>> {
        return clientDao.getAllClients()
    }

    fun getClientById(clientId: Int?): ClientListEntity {
        return clientDao.getClientById(clientId)
    }

    suspend fun getActualList(clientId: Int?): ClientList{
        return withContext(Dispatchers.IO){
            val actualList = getClientById(clientId)
            val toList = actualList.ToClientList()
            return@withContext toList
        }
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
    suspend fun deleteClientById(idClient:Int) {
        withContext(Dispatchers.IO){
            val delClient = getClientById(idClient)
            deleteClient(delClient)
        }
    }

    suspend fun updatePresuClient(clientId: Int?,newPresu: String) {
        withContext(Dispatchers.IO){
            val clientToUpdate = getClientById(clientId)
            val newClient =clientToUpdate.copy(presupuesto = newPresu)
            updateClient(newClient)
        }
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

    suspend fun deleteProductById(productId: Int?) {
        withContext(Dispatchers.IO){
            val clientToUpdate = getProductByID(productId)
            deleteProduct(clientToUpdate)
        }
    }

    fun getProductByID(productId: Int?): ProductEntity{
        return clientDao.getProductById(productId)
    }

    suspend fun updateChecked(productId: Int?){
        withContext(Dispatchers.IO){
            val actProd = getProductByID(productId)
            val checked = actProd.copy(checked = !actProd.checked)
            clientDao.updateProduct(checked)
        }
    }

    suspend fun updatePrice(productId: Int?,newPrice: Float){
        withContext(Dispatchers.IO){
            val actProd = getProductByID(productId)
            val newPriceProduct = actProd.copy(price = newPrice)
            clientDao.updateProduct(newPriceProduct)
        }
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


}