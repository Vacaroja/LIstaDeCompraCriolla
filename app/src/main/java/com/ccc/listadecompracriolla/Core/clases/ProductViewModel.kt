package com.ccc.listadecompracriolla.Core.clases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.listadecompracriolla.entities.ToClientList
import com.ccc.listadecompracriolla.entities.toProduct
import com.ccc.listadecompracriolla.pydolarnetwork.ApiDolarServices
import com.ccc.listadecompracriolla.repository.BcvRepository
import com.ccc.listadecompracriolla.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val bcvRepository: BcvRepository,
) : ViewModel() {

    //--------------------------StateFlow para el estado de carga-------------------------
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //----------------------------------TASA------------------------------------------------------
    // LiveData para los datos del BCV
    private val _bcvData = MutableStateFlow<ApiDolarServices?>(null)
    val bcvData: StateFlow<ApiDolarServices?> = _bcvData.asStateFlow()

    //StateFlow para el precio como Float
    private val _bcvPriceFloat = MutableStateFlow<Float?>(null)
    val bcvPriceFloat: StateFlow<Float?> = _bcvPriceFloat.asStateFlow()

    private val _tasa = MutableStateFlow(1f)
    val tasa: StateFlow<Float> = _tasa.asStateFlow()

    val isBcv: StateFlow<Boolean> = tasa.map { currentTasa ->
        currentTasa != 1f
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(200),
        initialValue = _tasa.value != 1f
    )

    //--------------------------------------EMPTYCLIENT------------------------------------------


    //-----------------------------------CLIENT LIST------------------------------------
    //--------------------------stateFlow para todas los clientes-------------------------
    private val _clientList = MutableStateFlow<List<ClientList>>(emptyList())
    val clientList: StateFlow<List<ClientList>> = _clientList.asStateFlow()

    //--------------------------stateFlow para el cliente actual-------------------------
    private val _actualList = MutableStateFlow(ClientList())
    val actualList: StateFlow<ClientList> = _actualList.asStateFlow()

    //---------------------------------PRODUCTOS---------------------------------------------------
    //-----------------------------all products-------------------------------
    private val _productos = MutableStateFlow<List<Product>>(emptyList())
    val productos: StateFlow<List<Product>> = _productos.asStateFlow()

    //-----------------------------actual product for actualize it
    private val _actualprod = MutableStateFlow(Product())
    val actualprod: StateFlow<Product> = _actualprod.asStateFlow()

    //--------------------------------presupuesto-------------------------------------
    private val _presupuesto = MutableStateFlow("")
    val presupuesto: StateFlow<String> = _presupuesto.asStateFlow()

    enum class SortType {
        NONE, ALPHABETICAL, REVERSE_ALPHABETICAL
    }

    private val _sortType = MutableStateFlow(SortType.NONE)
    val sortType = _sortType.asStateFlow()


    //--------------------------------total-------------------------------------
    val total: StateFlow<Float> =
        combine(
            _productos,
            _actualList,
            _tasa
        ) { products, actList, tasa ->//funcion que evalua los 2 stateFlows
            products.filter { it.client == actList.id }//filtro para actualList y Product
                .fold(0f) { acc, product ->//suma
                    acc + (product.price * product.cant)
                } * tasa
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(200),
                initialValue = 0f
            )


    //------------------------------------Total en carrito (productos marcados)------------------
    val inCar: StateFlow<Float> =
        combine(_productos, _actualList, _tasa) { products, actList, tasa ->
            products.filter { it.checked && it.client == actList.id }
                .fold(0f) { acc, product ->
                    acc + (product.price * product.cant)
                } * tasa
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(200),
                initialValue = 0f
            )
    //--------------------variable para verificar presupuesto excedido-------------------------------

    val deathPresu: StateFlow<Boolean> =
        combine(_presupuesto, inCar) { presu, incar ->//funcion que evalua los 2 stateFlows
            try {
                presu.toFloat() < incar
            } catch (_: Exception) {
                false
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(200),
                initialValue = false
            )
    //--------------------variable para verificar si la lista esta completa para el anuncio-------------------------------

    val completedActualList: StateFlow<Boolean> =
        combine(_productos, _actualList) { products, actList ->
            products.filter { it.client == actList.id }.all { it.checked }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(200),
                initialValue = false
            )
    val emptyClient: StateFlow<Boolean> = combine(_clientList) { client ->
        client.isEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(200),
        initialValue = false
    )
    val dividerCheckedItems: StateFlow<Boolean> = combine(_actualList, _productos) { client, prod ->
        prod.any { it.checked && it.client == client.id } && !prod.all { it.checked && it.client == client.id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(200),
        initialValue = false
    )

    val actualDobleProductList: StateFlow<Pair<List<Product>, List<Product>>> =
        combine(_productos, _actualList, _sortType) { prod, actList, currentSort ->
            val sortedList = when(currentSort) {
                SortType.NONE -> prod
                SortType.ALPHABETICAL -> prod.sortedBy { it.name  }
                SortType.REVERSE_ALPHABETICAL -> prod.sortedByDescending { it.name }
            }
            val filterList = sortedList.filter { it.client == actList.id }
            filterList.partition { !it.checked }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(200),
            initialValue = Pair(emptyList(), emptyList())
        )

    //--------------------------------------------INIT-----------------------------------------------
    init {
        viewModelScope.launch {
            clientRepository.getAllProducts().collect { productEntities ->
                _productos.value = productEntities.map { it.toProduct() }
            }
        }
        viewModelScope.launch {
            clientRepository.getAllClients().collect { clientListEntities ->
                _clientList.value = clientListEntities.map {
                    it.ToClientList()
                }
            }
        }
        viewModelScope.launch {
            actualizeProduct(-1)
        }
        loadActualList()


    }

    //---------------------------------------------Funciones-----------------------------------------------------


    //------------------------------changeCurrentList-----------------------------------------------

    //-----------------------------------------addClientList--------------------------------
    fun addClientList(nameClient: String) {
        val client = ClientList(null, nameClient)
        viewModelScope.launch {
            val clientEntities = client.ToClientListEntity()
            val newId = clientRepository.addClient(clientEntities)
            val clienWithId = client.copy(id = newId.toInt())
            _clientList.update { currentL ->
                currentL + clienWithId
            }
            changeCurrentList(newId.toInt())
        }
    }

    //-----------------------------------agregarProducto----------------------------------
    fun addProduct(producto: Product) {
        viewModelScope.launch {
            val prodEntities = producto.ProductEntity()
            val newId = clientRepository.addProduct(prodEntities)
            val productWithId = producto.copy(id = newId.toInt())
            _productos.update { currentList ->
                currentList + productWithId
            }
        }
    }

    fun addPresu(clientId: Int?, presu: String) {
        _presupuesto.value = presu
        _clientList.update { currentList ->
            currentList.map { client ->
                if (client.id == clientId) client.copy(presupuesto = presu)
                else client
            }
        }
        viewModelScope.launch {
            clientRepository.updatePresuClient(clientId, presu)
        }
    }

    fun getClient(idClient: Int?): ClientList? {
        return _clientList.value.firstOrNull { it.id == idClient }
    }


    fun saveActualList(idClientList: Int?) {
        viewModelScope.launch {
            clientRepository.saveLastClientId(idClientList!!)
        }
    }

    fun changeSortType(
        sort: SortType = SortType.NONE
    ) {
        _sortType.value = sort
    }

    fun changeCurrentList(clientId: Int?) {
        val currentList = getClient(clientId)
        if (currentList != null) {
            saveActualList(clientId)
            _actualList.value = currentList
            _presupuesto.value = currentList.presupuesto
        }
    }

    fun updateClient(idClient: Int?, newName: String) {
        val currentClient = getClient(idClient)
        if (currentClient != null) {
            val newClientName = currentClient.copy(name = newName)
            _clientList.update { currentList ->
                currentList.map { client ->
                    if (client.id == idClient) {
                        newClientName
                    } else
                        client
                }
            }
            viewModelScope.launch {
                val newName = newClientName.ToClientListEntity()
                clientRepository.updateClient(newName)
            }
        }
    }

    fun updateProduct(prod: Product) {
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == prod.id) {
                    prod
                } else {
                    product
                }
            }
        }
        viewModelScope.launch {
            val productEntitie = prod.ProductEntity()
            clientRepository.updateProduct(productEntitie)
        }
    }

    // Función para actualizar el precio
    fun updatePrecio(productId: Int?, newPrice: Float) {
        val newPriceXtasa = newPrice / _tasa.value
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == productId) {
                    product.copy(price = newPriceXtasa)
                } else {
                    product
                }
            }
        }
        viewModelScope.launch {
            clientRepository.updatePrice(productId, newPriceXtasa)
        }
    }

    fun toggleCheck(productId: Int?) {
        _productos.update { currentList ->
            val index = currentList.indexOfFirst { it.id == productId }
            if (index != -1) {
                val mutableList = currentList.toMutableList()

                // Get the product and toggle its 'checked' state
                val productToUpdate = mutableList[index]
                val updatedProduct = productToUpdate.copy(checked = !productToUpdate.checked)

                // Replace the old product with the updated one
                mutableList[index] = updatedProduct


                // Return the updated list
                mutableList
            } else currentList
        }
        viewModelScope.launch {
            clientRepository.updateChecked(productId)
        }
    }
    //------------------------------Organizar productos al inicio---------------------------------------


    //------------------------------actualizeProduct---------------------------------------

    fun actualizeProduct(productId: Int?) {
        if (productId == -1) {
            _actualprod.value = Product(0)
        } else {
            val actualProd = _productos.value.firstOrNull { it.id == productId }
            if (actualProd != null) {
                _actualprod.value = actualProd
            } else
                _actualprod.value = Product(0)
        }
    }

    fun changeBeforeDeleteList(idClientList: Int?) {
        //primero obtengo el valor de client list
        val clientList = _clientList.value

        //luego busco un elemento de la lista que no sea el que se va a borrar
        val changeList = clientList.firstOrNull { it.id != null && it.id != idClientList }

        //si encuentra alguno y la lista actual es igual a la que se quiere borrar se cambia sino eso quiere decir que es la unica lista y la lista actual vuelve a 0
        if (changeList != null && _actualList.value.id == idClientList) {
            changeCurrentList(changeList.id)
        }
    }

    //-----------------------------DeathPresupuestoChanged-----------------------


    fun loadActualList() {
        viewModelScope.launch {
            //primero busca el ultimo valor de lista guardado
            val lastClient = clientRepository.getLastClientId()
            //verifica si existe alguno
            val actualList =
                if (lastClient != -1) {
                    clientRepository.getLastList(lastClient)
                } else {
                    clientRepository.getActualList()
                }
            actualList?.let {
                _actualList.value = it
                _presupuesto.value = it.presupuesto
            }
        }
    }

    //--------------------------------------------Tasa--------------------------------------------
    //------------------------------------cargar valor de la database en BCV-------------------
    fun chargeDolarFromDB() {
        viewModelScope.launch {
            val responseFromDB = bcvRepository.getBcvData()
            if (responseFromDB != null) {
                val bcvPrice = responseFromDB.usd
                _bcvData.value = responseFromDB
                _bcvPriceFloat.value = bcvPrice?.toFloat()
            } else {
                _bcvPriceFloat.value = -2f
            }
        }
    }

    //--------------------------------actualizar tasa-------------------------------------
    fun actualizarTasa(
        tipoConversion: TipoConversion = TipoConversion.DIRECTA,
    ) {
        viewModelScope.launch {
            try {
                _tasa.value = when (tipoConversion) {
                    TipoConversion.DIRECTA -> 1f
                    TipoConversion.DOLAR_A_BCV -> bcvPriceFloat.value ?: 1f
                    TipoConversion.DOLAR_A_BS_USDT -> 129f
                }
            } catch (_: NumberFormatException) {
                tipoConversion
            }
        }
    }

    enum class TipoConversion { DIRECTA, DOLAR_A_BCV, DOLAR_A_BS_USDT }

    //------------------------------------------validar tasa-----------------------------------
    fun validTasa(): Boolean {
        return bcvPriceFloat.value == -1f || bcvPriceFloat.value == -2f
    }

    //------------------------------buscar la tasa---------------------------------------------
    fun searchDolarBcv() {
        _isLoading.value = true // Indica que la carga ha comenzado

        viewModelScope.launch {
            try {
                // Realiza la llamada a la API
                val response = bcvRepository.fetchAndSaveBcvFromApi()
                val df = DecimalFormat("#.##")

                if (response != null) {
                    _bcvData.value = response
                    _bcvPriceFloat.value = df.format(response.usd).toFloat()
                } else {
                    _bcvPriceFloat.value = -1f
                }
            } catch (_: Exception) {
                // Manejo de fallos de red o excepciones de parseo
                _bcvPriceFloat.value = -1f
            } finally {
                delay(1500)
                _isLoading.value = false // Indica que la carga ha terminado
            }
        }
    }


    //---------------------deleteProduct----------------------------
    fun deleteProduct(idProd: Int?) {
        //borrado en la lista local
        val product = _productos.value.toMutableList()
        product.removeIf { it.id == idProd }
        _productos.value = product.toList()
        //delete in database
        viewModelScope.launch {
            clientRepository.deleteProductById(idProd)
        }
    }

    fun deleteClient(idClient: Int?) {
        //primero convierte las dos listas en listas mutables para iterarlas mejor
        val clientList = _clientList.value.toMutableList()
        val product = _productos.value.toMutableList()
        //luego las borra donde encuentren la Id del cliente
        product.removeIf { it.client == idClient }
        clientList.removeIf { it.id == idClient }
        //luego las convierten nuevamente en listas para actualizar la lista actual
        _clientList.value = clientList.toList()
        _productos.value = product.toList()
        //por ultimo lo borra en la base de datos
        viewModelScope.launch {
            clientRepository.deleteClientById(idClient)
        }
    }


}

