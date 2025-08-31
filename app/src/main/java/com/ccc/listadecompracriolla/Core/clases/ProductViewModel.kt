package com.ccc.listadecompracriolla.Core.clases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccc.listadecompracriolla.pydolarnetwork.ApiDolarServices
import com.ccc.listadecompracriolla.pydolarnetwork.DolarApi
import com.ccc.listadecompracriolla.repository.ClientRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.text.toFloat

@HiltViewModel
class ProductViewModel @Inject constructor(private val clientRepository: ClientRepository) :
    ViewModel() {

    private val _clientList = MutableStateFlow<List<ClientList>>(emptyList())
    val clientList: StateFlow<List<ClientList>> = _clientList.asStateFlow()

    private val _actualList = MutableStateFlow(ClientList())
    val actualList: StateFlow<ClientList> = _actualList.asStateFlow()

    private val _productos = MutableStateFlow<List<Product>>(emptyList())
    val productos: StateFlow<List<Product>> = _productos.asStateFlow()

    private val _actualprod = MutableStateFlow(Product())
    val actualprod: StateFlow<Product> = _actualprod.asStateFlow()

    //--------------------------------presupuesto-------------------------------------
    private val _presupuesto = MutableStateFlow("")
    val presupuesto: StateFlow<String> = _presupuesto.asStateFlow()
    //--------------------variable para verificar presupuesto excedito
    private val _deathPresu = MutableStateFlow(false)
    val deathPresu: StateFlow<Boolean> = _deathPresu.asStateFlow()


    //--------------------------------total-------------------------------------
    val total: StateFlow<Float> =
        combine(_productos, _actualList) { products, actList ->//funcion que evalua los 2 stateFlows
            products.filter { it.client == actList.id }//filtro para actualList y Product
                .fold(0f) { acc, product ->//suma
                    acc + (product.price * product.cant)
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(200),
                initialValue = 0f
            )


    //------------------------------------Total en carrito (productos marcados)------------------
    val inCar: StateFlow<Float> = combine(_productos, _actualList) { products, actList ->
        products.filter { it.checked && it.client == actList.id }
            .fold(0f) { acc, product ->
                acc + (product.price * product.cant)
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(200),
            initialValue = 0f
        )

    //------------------------------------DATABASES-------------------------------------


    //--------------------------------------------INIT-----------------------------------------------
    init {

        _actualList.value = ClientList(id = 1, name = "Comida")
        _clientList.value = listOf(_actualList.value)


    }

    //---------------------------------------------Funciones-----------------------------------------------------
    //---------------------------------------------chargeCurrentProducts-------------------------------------


    //------------------------------changeCurrentList-----------------------------------------------
    fun changeCurrentList(clientId: Int?) {
        val currentList = _clientList.value.firstOrNull { it.id == clientId }
        if (currentList != null) {
            _actualList.value = currentList
            _presupuesto.value = currentList.presupuesto
            try {
                if (presupuesto.value.toFloat() < inCar.value) validDeathPresu(true) else validDeathPresu(false)
            }catch (_:Exception){
                validDeathPresu(false)
            }

        }

    }
    //-----------------------------------------addClientList--------------------------------

    fun addClientList(nameClient: String) {
        val idClient = _clientList.value.size + 1
        val client = ClientList(idClient, nameClient)
        viewModelScope.launch {
            _clientList.update { currentL ->
                currentL + client
            }
        }
        changeCurrentList(idClient)

    }


    //-----------------------------------agregarProducto----------------------------------
    fun addProduct(producto: Product) {
        viewModelScope.launch {
            _productos.update { currentList ->
                currentList + producto
            }
        }

    }

    fun updateCantidad(productId: Int, newCant: Float) {
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == productId) {
                    product.copy(cant = newCant)
                } else {
                    product
                }
            }
        }
    }

    fun updateProduct(productId: Int, prod: Product) {
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == productId) {
                    prod
                } else {
                    product
                }
            }
        }
    }

    // Función para togglear el check
    fun toggleCheck(productId: Int) {
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
    }

    // Función para actualizar el precio
    fun updatePrecio(productId: Int, newPrice: Float) {
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == productId) {
                    product.copy(price = newPrice)
                } else {
                    product
                }
            }
        }
    }
    //------------------------------actualizeProduct---------------------------------------

    fun actualizeProduct(productId: Int){
        if (productId == -1){
            _actualprod.value = Product(0)
        }
        else {
            val actualProd = _productos.value.firstOrNull { it.id == productId }
            if (actualProd != null){
                _actualprod.value = actualProd
            }else
                _actualprod.value = Product(0)
        }
    }

    //-----------------------------DeathPresupuestoChanged-----------------------

    fun validDeathPresu(change: Boolean) {
        _deathPresu.value = change
    }
    //-----------------------------addPresupuesto------------------------------

    fun addPresu(clientId: Int?, presu: String) {
        _presupuesto.value = presu
        _clientList.update { currentList ->
            currentList.map { client ->
                if (client.id == clientId) {
                    client.copy(presupuesto = presu)
                } else {
                    client
                }
            }
        }
    }

    //---------------------deleteProduct----------------------------
    fun deleteProduct(idProd: Int) {
        val product = _productos.value.toMutableList()
        product.removeIf { it.id == idProd }
        _productos.value = product.toList()
    }

    //----------------------------------------------BCV----------------------------
    // LiveData para los datos del BCV
    private val _bcvData = MutableStateFlow<ApiDolarServices?>(null)
    val bcvData: StateFlow<ApiDolarServices?> =
        _bcvData.asStateFlow() // Exponemos como StateFlow inmutable

    // 2. StateFlow para el estado de carga
    // Se inicializa en 'false' porque no estamos cargando al inicio.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 3. StateFlow para mensajes de error
    // Se inicializa con null, sin error al inicio.
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // 4. StateFlow para el precio como Float
    // Se inicializa con null.
    private val _bcvPriceFloat = MutableStateFlow<Float?>(null)
    val bcvPriceFloat: StateFlow<Float?> = _bcvPriceFloat.asStateFlow()

    //--------------------------------tasa-------------------------------------
    private val _tasa = MutableStateFlow(1f)
    val tasa: StateFlow<Float> = _tasa.asStateFlow()

    fun actualizarTasa(
        tipoConversion: TipoConversion = TipoConversion.DIRECTA
    ) {
        viewModelScope.launch {
            try {
                _tasa.value = when (tipoConversion) {
                    TipoConversion.DIRECTA -> 1f
                    TipoConversion.DOLAR_A_BCV -> bcvPriceFloat.value ?: 2.0f
                    TipoConversion.DOLAR_A_BS_USDT -> 129f
                }
            } catch (_: NumberFormatException) {
                tipoConversion
            }
        }
    }

    fun validTasa(): Boolean {
        return bcvPriceFloat.value == (-1).toFloat()
    }

    enum class TipoConversion { DIRECTA, DOLAR_A_BCV, DOLAR_A_BS_USDT }

    // La 'query' en searchDolarRate ya no es necesaria si el endpoint no la usa.
    // Si la API v2/dollar no necesita un parámetro en la URL, puedes eliminarlo.
    // He quitado el parámetro 'query' ya que la URL es fija para BCV.
    fun searchDolarBcv() {
        _isLoading.value = true // Indica que la carga ha comenzado
        _errorMessage.value = null // Limpia cualquier error previo

        viewModelScope.launch {
            try {
                // Realiza la llamada a la API
                val response = DolarApi.retrofitService.getData()
                val df = DecimalFormat("#.##")

                // Verifica la respuesta de la API
                // La API de Pydolarve usa un "status": "success" dentro del JSON
                // además del código HTTP 200.

                _bcvData.value = response

                _bcvPriceFloat.value = df.format(response.promedio).toFloat()

            } catch (_: Exception) {
                // Manejo de fallos de red o excepciones de parseo
                _bcvPriceFloat.value = -1f
            } finally {
                _isLoading.value = false // Indica que la carga ha terminado
            }
        }
    }
}

