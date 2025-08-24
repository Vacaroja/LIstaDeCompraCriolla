package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
    navigateToback: () -> Unit
) {
    var bottomSheetClient by remember { mutableStateOf(false) }
    val actualList by viewModel.actualList.collectAsState()

    TopAppBar(
        title = {
            Text(
                text = "${actualList.name}",
                modifier = Modifier.fillMaxWidth().clickable{bottomSheetClient = true},
                textAlign = TextAlign.Center
            )
//------------------------------------------NavigationIcons-----------------------------------------

        }, navigationIcon = {
            IconButton(onClick = {  }) {//BOTON PARA IR HACIA ATRAS
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

            IconButton(onClick = { viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BCV) }) {//BOTON PARA LAS CAMBIAR A DOLAR BCV
                Icon(
                    painter = painterResource(id = R.drawable.ic_bcv),
                    contentDescription = "bcv"
                )
            }
//------------------------------------------DolarToUSDT-----------------------------------------
            /*
                        IconButton(onClick = {viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BS_USDT)}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_bolivar),
                                contentDescription = " cambio a bolivares"
                            )
                        }

             */
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
    if (bottomSheetClient){
        ModalBottomSheetClientList(viewModel = viewModel,onDismiss= { bottomSheetClient = false },onChange = {currentList -> viewModel.changeCurrentList(currentList)})
    }


}