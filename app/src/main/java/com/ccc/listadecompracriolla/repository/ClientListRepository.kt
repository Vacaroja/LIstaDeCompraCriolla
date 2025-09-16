package com.ccc.listadecompracriolla.repository

import android.content.SharedPreferences
import com.ccc.listadecompracriolla.Core.clases.ClientList
import com.ccc.listadecompracriolla.dao.ClientDao
import com.ccc.listadecompracriolla.entities.ClientListEntity
import com.ccc.listadecompracriolla.entities.ProductEntity
import com.ccc.listadecompracriolla.entities.ToClientList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class ClientRepository @Inject constructor(
    private val clientDao: ClientDao,
    private val sharedPreferences: SharedPreferences
) {

    //---------------------------------------SHARED PREFERENCES-----------------------------

    companion object {
        const val KEY_LAST_CLIENT_ID = "last_client_id"
    }

    fun saveLastClientId(id: Int) {
        sharedPreferences.edit { putInt(KEY_LAST_CLIENT_ID, id) }
    }

    fun getLastClientId(): Int {
        // -1 es el valor por defecto si la clave no se encuentra
        return sharedPreferences.getInt(KEY_LAST_CLIENT_ID, -1)
    }

    //------------------------------------------CRUD-------------------------------------------

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return clientDao.getAllProducts()
    }

    // Opcional: Obtener todos los productos en general
    fun getAllClients(): Flow<List<ClientListEntity>> {
        return clientDao.getAllClients()
    }

    fun getClientById(clientId: Int?): ClientListEntity? {
        return clientDao.getClientById(clientId)
    }

    fun getProductByID(productId: Int?): ProductEntity {
        return clientDao.getProductById(productId)
    }

    suspend fun getLastList(idClient: Int?): ClientList? {
        return withContext(Dispatchers.IO){
            val lastClientEntities = getClientById(idClient)
            val lastClient = lastClientEntities?.ToClientList()
            return@withContext lastClient
        }
    }

    suspend fun getActualList(): ClientList? {
        return withContext(Dispatchers.IO) {
            val actualList = clientDao.getFirstId()
            val toList = actualList?.ToClientList()
            return@withContext toList
        }
    }

    //------------------------------------------CRUD-------------------------------------------

    //--------------------------------------ADD--------------------------------------------
    suspend fun addClient(client: ClientListEntity): Long {
        return clientDao.insertClient(client)
    }

    suspend fun addProduct(product: ProductEntity): Long { //Metodo general para añadir un producto
        return clientDao.insertProduct(product)
    }

    //------------------------------------UPDATE-----------------------------------------
    suspend fun updateClient(client: ClientListEntity) {
        clientDao.updateClient(client)
    }


    suspend fun updatePresuClient(clientId: Int?, newPresu: String) {
        withContext(Dispatchers.IO) {
            val clientToUpdate = getClientById(clientId)
            if (clientToUpdate != null){
                val newClient = clientToUpdate.copy(presupuesto = newPresu)
                updateClient(newClient)
            }
        }
    }

    suspend fun updateProduct(product: ProductEntity) {
        clientDao.updateProduct(product)
    }


    suspend fun updateChecked(productId: Int?) {
        withContext(Dispatchers.IO) {
            val actProd = getProductByID(productId)
            val checked = actProd.copy(checked = !actProd.checked)
            clientDao.updateProduct(checked)
        }
    }

    suspend fun updateAllCheckedByClient(client: Int?,toggle: Boolean) {
        withContext(Dispatchers.IO) {
            clientDao.updateAllProductsChecked(client,toggle)
        }
    }

    suspend fun updatePrice(productId: Int?, newPrice: Float) {
        withContext(Dispatchers.IO) {
            val actProd = getProductByID(productId)
            val newPriceProduct = actProd.copy(price = newPrice)
            clientDao.updateProduct(newPriceProduct)
        }
    }

    //------------------------------------DELETE-----------------------------------------

    suspend fun deleteClient(client: ClientListEntity) {
        clientDao.deleteClient(client)
    }

    suspend fun deleteClientById(idClient: Int?) {
        withContext(Dispatchers.IO) {
            val delClient = getClientById(idClient)
            if (delClient != null){
                deleteClient(delClient)
            }
        }
    }

    suspend fun deleteProduct(product: ProductEntity) {
        clientDao.deleteProduct(product)
    }

    suspend fun deleteProductByClient(clientId: Int?) {
        withContext(Dispatchers.IO) {
            clientDao.deleteProductByClientId(clientId)
        }
    }

    suspend fun deleteProductById(productId: Int?) {
        withContext(Dispatchers.IO) {
            val clientToUpdate = getProductByID(productId)
            deleteProduct(clientToUpdate)
        }
    }


}