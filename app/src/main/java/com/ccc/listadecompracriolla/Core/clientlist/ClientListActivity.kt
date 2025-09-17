@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core.clientlist


import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.clases.VersionManager
import com.ccc.listadecompracriolla.Core.clientlist.drawer.NavigationDrawerClientList
import com.ccc.listadecompracriolla.Core.clientlist.principal.LazyListClient
import com.ccc.listadecompracriolla.Core.clientlist.top.TopMenu
import com.ccc.listadecompracriolla.Core.versionmanager.ForcedUpdateDialog
import com.ccc.listadecompracriolla.adds.loadInterstitialAd
import com.ccc.listadecompracriolla.adds.showInterstitialAd
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToCreateFood: () -> Unit,
    onAbout:() -> Unit
) {
//------------------------------------------Variables-----------------------------------------

    //var for pull to refresh
    val loading by viewModel.isLoading.collectAsState()
    val refreshState = rememberPullToRefreshState()

    val scrollState = rememberLazyListState()
    val isExtended by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }//var for state of the FAB
    //variables from viewmodels

    val isCompleted by viewModel.completedActualList.collectAsState()


    var showUpdateDialog by remember { mutableStateOf(false) }
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
    val appVersionManager = remember { VersionManager(context) }



    LaunchedEffect(Unit) {
        appVersionManager.checkForUpdates {
        showUpdateDialog = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawerClientList{onAbout()}
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

                    if (enableButton && !isEmptyClient) {
                        LazyListClient(
                            innerpadding = innerpadding,
                            viewModel = viewModel,
                            scrollState = scrollState,
                            onNavigateToCreateFood = {
                                enableButton = false
                                navigateToCreateFood()
                            }
                        )
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
        if (showUpdateDialog){
            ForcedUpdateDialog {
                showUpdateDialog = false
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







