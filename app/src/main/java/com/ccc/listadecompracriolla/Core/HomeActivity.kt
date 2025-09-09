@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.ui.theme.Orange
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigateToClient: () -> Unit) {
//------------------------------------------variables de estado-----------------------------------------

    var openDialog by remember { mutableStateOf(false) }


//------------------------------------------Scaffold-----------------------------------------

    Scaffold(
        topBar = { TopBarHome() },
        bottomBar = { BottomBarHome() },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    openDialog = true
                },
                modifier = Modifier.padding(16.dp),
                icon = { Icon(Icons.Filled.Add, "Agregar") },
                text = { Text("Nueva Lista") }
            )
        },
    ) { innerpadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerpadding), horizontalAlignment = Alignment.CenterHorizontally

        ) {
//------------------------------------------Texto principal-----------------------------------------

            Spacer(modifier.weight(1f))
            Text(
                text = "Presione el botón + para crear una nueva lista de compras",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier.weight(1f))

//------------------------------------------MostrarAlCrearLista-----------------------------------------

        }
        if (openDialog) CreateListScreenDialog(
            onDismiss = {openDialog = false },
            onConfirm = {
                openDialog = false
                navigateToClient()
            }
        )

    }
}
//------------------------------------------BottomBar-----------------------------------------

@Composable
fun BottomBarHome() {
    BottomAppBar(containerColor = Orange) { }
}

//------------------------------------------TopBar-----------------------------------------

@Composable
fun TopBarHome(modifier: Modifier = Modifier,
               ) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
//------------------------------------------HorizontalSheet-----------------------------------------

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Spacer(Modifier.height(12.dp))
            ModalDrawerSheet {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Lista de Compra Criolla",
                            fontSize = 20.sp
                        )
                    },
                    selected = false,
                    onClick = { /* Handle click */ },
                    badge = {
                        Image(Icons.Default.Info,
                            contentDescription = "",
                            modifier.size(30.dp)
                        )
                    }
                )

                HorizontalDivider()
                NavigationDrawerItem(//pantalla de ajustes
                    label = { Text("Apoyanos") },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Settings, "ajustes") }
                )
                NavigationDrawerItem(//pantalla de acerca de la aplicacion
                    label = { Text("Premiun") },
                    selected = false,
                    onClick = { /* Handle click */ },
                    icon = { Icon(imageVector = Icons.Default.Info, "Acerca de") }
                )

            }

        },
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Lista de compra criolla",
                    fontWeight = FontWeight.Bold
                )
                modifier.padding(15.dp)
            },
            navigationIcon = {
//------------------------------------------EstadoDelHorizontalSheet-----------------------------------------

                IconButton(onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {

//------------------------------------------SettingsButton-----------------------------------------

                IconButton(onClick = {}) {//BOTON PARA LAS OPCIONES
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Settings"
                    )
                }
            }, colors = TopAppBarColors(
                containerColor = Orange,
                scrolledContainerColor = Orange,
                navigationIconContentColor = Color.Black,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Unspecified
            )
        )


    }
}
//------------------------------------------AlertDialog-----------------------------------------

@Composable
fun CreateListScreenDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var nameList by remember { mutableStateOf("") }
    AlertDialog(
        confirmButton = {
            TextButton(
                onClick = { onConfirm(nameList) },
                enabled = nameList.isNotEmpty()
            ) { Text(text = "Registrar lista") }
        },
        dismissButton = { TextButton(onClick = { onDismiss() }) { Text("Cancelar") } },
        title = { Text(text = "Coloque el Nombre de la lista", textAlign = TextAlign.Center) },
        text = {
            OutlinedTextField(
                value = nameList,
                onValueChange = { nameList = it },
                label = { Text("Lista") },
                placeholder = { Text("Coloque el nombre de la lista") })
        },
        onDismissRequest = { onDismiss() })
}

//------------------------------------------Preview-----------------------------------------

@Composable
fun HomePreview() {
    HomeScreen { }
}
