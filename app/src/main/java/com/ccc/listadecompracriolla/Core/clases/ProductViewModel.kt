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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val clientRepository: ClientRepository): ViewModel() {
    private val _productos = MutableStateFlow<List<Product>>(emptyList())
    val productos: StateFlow<List<Product>> = _productos.asStateFlow()

    //--------------------------------presupuesto-------------------------------------
    private val _presupuesto = MutableStateFlow("")
    val presupuesto: StateFlow<String> = _presupuesto.asStateFlow()

    /*val presupuestoExcedido: StateFlow<Boolean> = _productos
        .combine(_presupuesto) { productos, presupuesto? ->
            val totalActual = productos.fold(0f) { acc, p -> acc + (p.price * p.cant) }
            totalActual > presupuesto
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )*/
    
    //--------------------------------tasa-------------------------------------

    //--------------------------------total-------------------------------------
    val total: StateFlow<Float> = _productos
        .map { products ->
            products.fold(0f) { acc, product ->
                acc + (product.price * product.cant)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(200),
            initialValue = 0f
        )

    //------------------------------------Total en carrito (productos marcados)------------------
    val inCar: StateFlow<Float> = _productos.map { products ->
        products
            .filter { it.checked }
            .fold(0f) { acc, product ->
                acc + (product.price * product.cant)
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(200),
            initialValue = 0f
        )
//---------------------------------------------Funciones-----------------------------------------------------

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

    // Función para togglear el check
    fun toggleCheck(productId: Int) {
        _productos.update { currentList ->
            currentList.map { product ->
                if (product.id == productId) {
                    product.copy(checked = !product.checked)
                } else {
                    product
                }
            }
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
    //-----------------------------addPresupuesto------------------------------

    fun addPresu(Presu: String){
        _presupuesto.value = Presu
    }


//----------------------------------------------BCV----------------------------
    // LiveData para los datos del BCV
    private val _bcvData = MutableStateFlow<ApiDolarServices?>(null)
    val bcvData: StateFlow<ApiDolarServices?> = _bcvData.asStateFlow() // Exponemos como StateFlow inmutable

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
            } catch (e: NumberFormatException) {
                tipoConversion
            }
        }
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

            } catch (e: Exception) {
                // Manejo de fallos de red o excepciones de parseo
                _bcvPriceFloat.value = -1f
            } finally {
                _isLoading.value = false // Indica que la carga ha terminado
            }
        }
    }
}

