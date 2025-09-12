@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core.clientlist
/*
orden alfabetico
filtrar por orden  y colocar las ya seleccionadas abajo*/

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.adds.loadInterstitialAd
import com.ccc.listadecompracriolla.adds.showInterstitialAd
import com.ccc.listadecompracriolla.ui.theme.Blue
import com.ccc.listadecompracriolla.ui.theme.Red
import com.ccc.listadecompracriolla.ui.theme.amarillo
import com.ccc.listadecompracriolla.ui.theme.black
import com.ccc.listadecompracriolla.ui.theme.oro
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToCreateFood: () -> Unit,
) {
//------------------------------------------Variables-----------------------------------------
    //var for pull to refresh
    val loading by viewModel.isLoading.collectAsState()
    val refreshState = rememberPullToRefreshState()

    val scrollState = rememberLazyListState()
    val isExtended by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }//var for state of the FAB
    //variables from viewmodels
    val productos by viewModel.productos.collectAsState()
    val clients by viewModel.actualList.collectAsState()
    val isCompleted by viewModel.completedActualList.collectAsState()


    val (uncheckedItems, checkedItems) = remember(productos) {
        productos.partition { !it.checked }
    }
    val dividerCheckedItems = remember(productos) {
        productos.any { it.checked && it.client == clients.id }
    }
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val context = LocalContext.current
    val activity = context as? Activity

    val titleDrawerBrush = Brush.horizontalGradient(listOf(amarillo, Blue, Red))
    val titleDrawerPaddingValues =
        PaddingValues(start = 15.dp, top = 30.dp, end = 5.dp, bottom = 5.dp)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Spacer(Modifier.height(12.dp))
            ModalDrawerSheet(
                modifier = modifier
                    .requiredWidth(250.dp)
                    .fillMaxHeight()
            ) {

                Text(
                    text = "Lista de Compra Criolla",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge.copy(brush = titleDrawerBrush),
                    modifier = modifier.padding(titleDrawerPaddingValues),
                    fontWeight = FontWeight.ExtraBold,

                    )

                HorizontalDivider()
                NavigationDrawerItem(//pantalla de ajustes
                    label = { Text("Ajustes") },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Settings, "ajustes") }
                )
                NavigationDrawerItem(//pantalla de acerca de la aplicacion
                    label = { Text("Premiun", color = oro) },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Star, "Premiun",tint = oro) }
                )
                NavigationDrawerItem(//pantalla de acerca de la aplicacion
                    label = { Text("Apoyanos ", color = Red) },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Favorite, "Apoyanos",tint = Red) }
                )
                NavigationDrawerItem(//pantalla de acerca de la aplicacion
                    label = { Text("Info de app ") },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Info, "info") }
                )
            }
        },
    ) {
        PullToRefreshBox(state = refreshState, isRefreshing = loading, onRefresh = {
            coroutineScope.launch {
                viewModel.searchDolarBcv()
            }
        }) {
            //------------------------------------------Variables-----------------------------------------

            Scaffold(
                topBar = {
                    TopMenu(
                        viewModel = viewModel,
                        navigateToback = { },
                        onOpenDrawer = {
                            coroutineScope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
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
                        },
                    )
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
                    focusManager.clearFocus()
                    stateOfBalance = false
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
                                items = uncheckedItems.filter { it.client == clients.id },
                                key = { it.id!! }) { producto ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    ProducIterator(
                                        product = producto,
                                        viewModel = viewModel,
                                        onChangeProduct = { idProduct ->
                                            enableButton = false
                                            viewModel.actualizeProduct(idProduct)
                                            navigateToCreateFood()
                                        },
                                    )
                                }
                            }
                            if (dividerCheckedItems) {
                                item {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        HorizontalDivider(
                                            modifier
                                                .weight(1f)
                                                .padding(start = 8.dp),
                                            thickness = 2.dp,
                                            color = black
                                        )
                                        Text(
                                            "Listos",
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        HorizontalDivider(
                                            modifier
                                                .weight(1f)
                                                .padding(end = 8.dp),
                                            thickness = 2.dp,
                                            color = black
                                        )
                                    }
                                }
                            }
                            items(
                                items = checkedItems,
                                key = { it.id!! }) { producto ->
                                if (producto.client == clients.id) {
                                    AnimatedVisibility(
                                        visible = true,
                                        enter = fadeIn() + slideInVertically(),
                                        exit = fadeOut() + slideOutVertically()

                                    ) {
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
    if (isCompleted) {
        if (activity != null) {
            showInterstitialAd(activity)
            loadInterstitialAd(context)
        }
    }
}







