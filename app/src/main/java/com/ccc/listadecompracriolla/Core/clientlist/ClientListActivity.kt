@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core.clientlist
/*
Terminar boton presupuesto
retornar producto de CreateFoodScreen
Colocar Scroll
colocar animaciones
orden alfabetico
filtrar por orden  y colocar las ya seleccionadas abajo
volver clickeable las cards y textbutton
 viewModel.addProduct(
                        producto = Product(
                            1,
                            "Leche",
                            1f,
                            5.5f
                        )) */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToCreateFood: () -> Unit,
) {
//------------------------------------------Variables-----------------------------------------
    //var for pull to refresh
    var loading by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()

    val scrollState = rememberLazyListState()
    val isExtended by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }//var for state of the FAB
    //variables from viewmodels
    val productos by viewModel.productos.collectAsState()
    val clients by viewModel.actualList.collectAsState()
    //variable to state of balance
    var stateOfBalance by remember { mutableStateOf(false) }

    val isEmptyClient by viewModel.emptyClient.collectAsState()
    var enableButton by remember { mutableStateOf(true) }

    //variable corrutinas
    val coroutineScope = rememberCoroutineScope()
    //snackBarState
    val snackbarHostState = remember { SnackbarHostState() }
    //variables para enfocar
    val focusManager = LocalFocusManager.current


    PullToRefreshBox(state = refreshState, isRefreshing = loading, onRefresh = {
        coroutineScope.launch {
            loading = true
            viewModel.searchDolarBcv()
            delay(2.seconds)
            loading = false
        }
    }) {
        //------------------------------------------Variables-----------------------------------------


        Scaffold(
            topBar = {
                TopMenu(
                    viewModel = viewModel,
                    navigateToback = { },
                    onFailureApi = {//on failure Api show snackBar to internet
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Debe conectarse a internet para usar el conversor de tasas",
                                actionLabel = "Ya estoy conectado",
                                duration = SnackbarDuration.Short
                            )
                            when (result) {
                                SnackbarResult.Dismissed -> {
                                    viewModel.chargeDolarFromDB()
                                    if (viewModel.validTasa()) {
                                        snackbarHostState.showSnackbar(
                                            "Se usara la ultima tasa guardada, recargue para volver a intenter buscar la tasa",
                                            actionLabel = "OK"
                                        )
                                    } else {
                                        snackbarHostState.showSnackbar(
                                            "No se encuentra ninguna tasa guardada para usar, conectese a internet si quiere usar el convertidor de tasas",
                                            actionLabel = "OK"
                                        )
                                    }
                                }

                                SnackbarResult.ActionPerformed -> {
                                    viewModel.searchDolarBcv()
                                }
                            }
                        }
                    })
            },
            bottomBar = { BottomClientList(viewModel = viewModel) },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                viewModel.isEmptyClient()
                if (enableButton && !isEmptyClient) {
                    //animacion del floatingActionButton
                    AnimatedVisibility(
                        visible = isExtended,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
//------------------------------------------FBA-----------------------------------------

                        ExtendedFloatingActionButton(

                            onClick = {
                                navigateToCreateFood()
                                enableButton = false
                            },
                            icon = { Icon(Icons.Default.Add, "Agregar producto") },
                            text = { Text("AGREGAR") }, // Texto visible solo cuando no hay scroll
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    if (!isExtended) {

                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                enableButton = false
                                navigateToCreateFood()
                            }
                        ) {
                            Icon(Icons.Default.Add, "Agregar PRODUCTO")
                        }
                    }
                }
            }
        ) { innerpadding ->
//------------------------------------------iterationOfProducts-----------------------------------------
            Box(modifier = modifier.clickable {
                //var to hide keyboard if its show
                focusManager.clearFocus();stateOfBalance = false
            }) {
                viewModel.isEmptyClient()
                if (enableButton && !isEmptyClient) {

                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(innerpadding),
                        state = scrollState,
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        //reciclerview to watch items or products
                        items(
                            items = productos,
                            key = { it.id!! }) { producto ->
                            if (producto.client == clients.id) {
                                ProducIterator(
                                    product = producto,
                                    viewModel = viewModel,
                                    onChangeProduct = { idProduct ->
                                        enableButton = false
                                        viewModel.actualizeProduct(idProduct)
                                        navigateToCreateFood()
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = modifier
                            .align(Alignment.BottomStart)
                            .padding(innerpadding)
                        // Asegura que el botón esté por encima
                    ) {
                        ClientBalance(viewModel, stateOfBalance = stateOfBalance) {
                            stateOfBalance = !stateOfBalance
                        }


                    }

                }
            }


        }


    }
}


fun formatNumber(number: Float): String {
    // Puedes especificar la configuración regional (Locale) aquí.
    // Locale.US usa comas para miles y puntos para decimales (ej. 1,234.56).
    // Locale.getDefault() usa la configuración regional del dispositivo.
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    // Si quisieras que el formato usara puntos para miles y comas para decimales
    // (común en muchos países de Europa y América Latina), usarías una Locale diferente,
    // por ejemplo, Locale("es", "ES") para España o Locale("es", "VE") para Venezuela.
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "ES")) // Ejemplo para España
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "VE")) // Ejemplo para Venezuela

    return formatter.format(number)
}




