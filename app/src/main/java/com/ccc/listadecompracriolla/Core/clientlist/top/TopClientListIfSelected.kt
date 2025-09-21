package com.ccc.listadecompracriolla.Core.clientlist.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ccc.listadecompracriolla.Core.clientlist.alertbox.AlertBoxDeleteClient
import com.ccc.listadecompracriolla.ui.theme.OrangeBlack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopClientListSelected(
    isOneSelected: Boolean,
    modifier: Modifier,
    onDeleteSelected: () -> Unit,
    onChanged: () -> Unit,
    onBack: () -> Unit,
) {
    var isDelete by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
//------------------------------------------NavigationIcons-----------------------------------------
        }, navigationIcon = {
            IconButton(onClick = { onBack() }) {//BOTON PARA DRAWER
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
        }, actions = {
            Spacer(modifier = modifier.weight(1f))
            AnimatedVisibility(isOneSelected) {
                IconButton(onClick = { onChanged() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Modificar")
                }
            }
            IconButton(onClick = { onDeleteSelected() }) {
                Icon(Icons.Default.Delete, contentDescription = "Borrar seleccionados")
            }

        },
//------------------------------------------colores-----------------------------------------
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = OrangeBlack,
            scrolledContainerColor = OrangeBlack,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
        )
    )
    if (isDelete) {
        AlertBoxDeleteClient(
            anyText = "LOS PRODUCTOS SELECCIONADOS?",
            onDismiss = { isDelete = false }
        ) { }
    }
}