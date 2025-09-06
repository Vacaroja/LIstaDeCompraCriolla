package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToback: () -> Unit,
    onFailureApi: () -> Unit
) {
    var bottomSheetClient by remember { mutableStateOf(false) }
    var bottomSheetNewClient by remember { mutableStateOf(false) }
    val actualList by viewModel.actualList.collectAsState()
    val isEmptyClient by viewModel.emptyClient.collectAsState()

    TopAppBar(
        title = {
            viewModel.isEmptyClient()
            if (isEmptyClient){
                Text(
                    text ="AÑADIR LISTA",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { bottomSheetNewClient = true },
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "AÑADIR LISTA"
                )
            }else {
                if (actualList.id == 0){
                    Text(
                        text ="CAMBIAR LISTA",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bottomSheetClient = true },
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "CAMBIAR LISTA"
                    )
                } else{
                    Text(
                        text ="${actualList.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bottomSheetClient = true },
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "CAMBIAR LISTA"
                    )
                }
            }

//------------------------------------------NavigationIcons-----------------------------------------

        }, navigationIcon = {
            IconButton(onClick = { }) {//BOTON PARA IR HACIA ATRAS
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Back"
                )
            }


        }, actions = {
//------------------------------------------DolarToDolar-----------------------------------------

            IconButton(onClick = { viewModel.actualizarTasa(ProductViewModel.TipoConversion.DIRECTA) }) {//BOTON PARA LAS CAMBIAR A DOLAR
                Icon(
                    painter = painterResource(id = R.drawable.dolar),
                    contentDescription = "Settings"
                )
            }
//------------------------------------------VerticalDivider-----------------------------------------

            VerticalDivider(
                color = Color.Black, thickness = 2.dp,
                modifier = modifier.padding(
                    vertical = 7.dp
                )
            )
//------------------------------------------DolarToBCV-----------------------------------------

            IconButton(onClick = {
                if (viewModel.validTasa()) {
                    onFailureApi()
                } else {
                    viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BCV)
                }
            }) {//BOTON PARA LAS CAMBIAR A DOLAR BCV
                Icon(
                    painter = painterResource(id = R.drawable.ic_bcv),
                    contentDescription = "bcv"
                )
            }

//------------------------------------------ButtonMore-----------------------------------------

            IconButton(onClick = {}) {//BOTON PARA LAS OPCIONES
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
        },
//------------------------------------------colores-----------------------------------------
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange,
            scrolledContainerColor = Orange,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,

            )
    )
    if (bottomSheetClient) {
        ModalBottomSheetClientList(
            viewModel = viewModel,
            onDismiss = { bottomSheetClient = false },
            onChange = { currentList -> viewModel.changeCurrentList(currentList) },
            onAddNew = {
                bottomSheetNewClient = true
                bottomSheetClient = false},
            onDelete = {clientToDelete ->
                viewModel.changeBeforeDeleteList(clientToDelete)
                viewModel.deleteClient(clientToDelete)

            })
    }
    if (bottomSheetNewClient){
        CreateClientList(
            onSave = {
                nameClient -> viewModel.addClientList(nameClient)
                bottomSheetNewClient = false},
            onDismiss = {
                bottomSheetNewClient = false }
        )
    }



}

