package com.ccc.listadecompracriolla.Core.clientlist.top

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.ccc.listadecompracriolla.ui.theme.OrangeBlack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    onFailureApi: () -> Unit,
    onOpenDrawer: () -> Unit,

) {
    var bottomSheetClient by remember { mutableStateOf(false) }
    var bottomSheetNewClient by remember { mutableStateOf(false) }
    var isExpandedMenu by remember { mutableStateOf(false) }


    val isPressed by viewModel.isBcv.collectAsState()

    var changedClientName: String? by remember { mutableStateOf(null) }
    var changedClientId: Int? by remember { mutableStateOf(null) }

    val actualList by viewModel.actualList.collectAsState()
    val isEmptyClient by viewModel.emptyClient.collectAsState()

    val animatedColorBs by animateColorAsState(
        targetValue = if (isPressed) OrangeBlack else Orange,
        animationSpec = tween(durationMillis = 200)
    )
    val animatedColorDolar by animateColorAsState(
        targetValue = if (!isPressed) OrangeBlack else Orange,
        animationSpec = tween(durationMillis = 200)
    )
    TopAppBar(
        title = {
            if (isEmptyClient) {
                Text(
                    text = "AÑADIR LISTA",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { bottomSheetNewClient = true },
                    textAlign = TextAlign.Center
                )

            } else {
                if (actualList.id == 0) {
                    Text(
                        text = "CAMBIAR LISTA",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bottomSheetClient = true },
                        textAlign = TextAlign.Center
                    )

                } else {
                    Text(
                        text = "${actualList.name}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bottomSheetClient = true },
                        textAlign = TextAlign.Center
                    )
                }
            }

//------------------------------------------NavigationIcons-----------------------------------------

        }, navigationIcon = {
            IconButton(onClick = { onOpenDrawer()}) {//BOTON PARA DRAWER
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Back"
                )
            }


        }, actions = {
//------------------------------------------DolarToDolar-----------------------------------------

            Card(
                modifier.clickable {
                    viewModel.actualizarTasa(ProductViewModel.TipoConversion.DIRECTA)
                },
                colors = CardDefaults.cardColors(
                    containerColor = animatedColorDolar
                )
            ) {//BOTON PARA LAS CAMBIAR A DOLAR
                Icon(
                    painter = painterResource(id = R.drawable.dolar),
                    contentDescription = "Settings",
                    modifier = modifier.size(45.dp)
                )
            }
//------------------------------------------VerticalDivider-----------------------------------------

            VerticalDivider(
                color = Color.Black, thickness = 2.dp,
                modifier = modifier.padding(
                    top = 3.dp,
                    bottom = 4.dp,
                    start = 4.dp,
                    end = 2.dp
                )
            )
//------------------------------------------DolarToBCV-----------------------------------------

            Card(
                modifier.clickable {

                    if (viewModel.validTasa()) {
                        onFailureApi()
                    } else {
                        viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BCV)
                    }
                },
                colors = CardDefaults.cardColors(
                    containerColor = animatedColorBs
                )
            ) {//BOTON PARA LAS CAMBIAR A DOLAR BCV
                Icon(
                    painter = painterResource(id = R.drawable.bcv_icon),
                    contentDescription = "bcv",
                    modifier = modifier.size(50.dp)
                )
            }

//------------------------------------------ButtonMore-----------------------------------------

            IconButton(onClick = {isExpandedMenu = true}) {//BOTON PARA LAS OPCIONES
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
            DropDownMenuTop(
                expanded = isExpandedMenu,
                viewModel = viewModel,
                onExpandex = { isExpandedMenu = !isExpandedMenu },
                onDeleteAll = { viewModel.deleteAllProductByClientId(actualList.id) },
                onAllToggle = {toggle -> viewModel.toggleCheckAllByClient(actualList.id,toggle)}
            )
        },
//------------------------------------------colores-----------------------------------------
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange,
            scrolledContainerColor = Orange,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
        )
    )
    if (bottomSheetClient) {
        ModalBottomSheetClientList(
            viewModel = viewModel,
            onDismiss = { bottomSheetClient = false },
            onChange = { currentList -> viewModel.changeCurrentList(currentList) },
            onAddNew = {
                bottomSheetNewClient = true
                bottomSheetClient = false
            },
            onDelete = { clientToDelete ->
                if (clientToDelete != null){
                    viewModel.changeBeforeDeleteList(clientToDelete)
                    viewModel.deleteClient(clientToDelete)
                }

            },
            onChangeName = { nameClient, idClient ->
                changedClientName = nameClient
                changedClientId = idClient
                bottomSheetNewClient = true
                bottomSheetClient = false
            })
    }
    if (bottomSheetNewClient) {
        CreateClientList(
            lastName = changedClientName,
            onSave = { nameClient ->
                viewModel.addClientList(nameClient)
                bottomSheetNewClient = false
            },
            onDismiss = {
                bottomSheetNewClient = false
            },
            onChangeName = { newName ->
                viewModel.updateClient(changedClientId, newName)
                viewModel.changeCurrentList(changedClientId)
                changedClientName = null
                changedClientId = null
                bottomSheetNewClient = false
            }
        )
    }


}

