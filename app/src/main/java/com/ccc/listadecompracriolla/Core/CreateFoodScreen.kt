@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core
/*Cosas por hacer
//cambiar tipo de teclado en selector de cantidad y precio
//cambiar por lista desplegable la medida
Mejorar Topbar y colocar "LISTO"
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToback: () -> Unit
) {
//------------------------------------------variables de estado-----------------------------------------
    val medidaList = listOf("Und", "Lb", "Kg", "L")
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf(medidaList[0]) }
    var cantidad by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var dialogErrorAlert by remember { mutableStateOf(false) }
    val productos by viewModel.productos.collectAsState()





    Scaffold(
//------------------------------------------topbar-----------------------------------------

        topBar = {
            TopMenuCreateFood(
                navigateToback = { navigateToback() },
                saveProduct = {
                    if (nombre.isBlank()) {
                        dialogErrorAlert = true
                    } else {
                        val nuevoProducto = Product(
                            id = productos.size + 1, // O usa un UUID
                            name = nombre,
                            cant = if (cantidad.isNotEmpty()) cantidad.toFloat() else 1f,
                            price = if (precio.isNotEmpty()) precio.toFloat() else 0f,
                            nota = nota,
                            medida = unidad
                        )
                        viewModel.addProduct(nuevoProducto)
                        navigateToback()
                        // Opcional: regresar después de guardar

                    }

                }
            )
        },
//------------------------------------------bottombar-----------------------------------------

        bottomBar = { BottombarCreateFood() }//bottom bar por si quiero colocar una mariquera
    )
//------------------------------------------initContent-----------------------------------------

    { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//------------------------------------------nombre-----------------------------------------


            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("NOMBRE") },
                placeholder = { Text("COLOQUE EL NOMBRE DEL PRODUCTO") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "costo"
                    )
                },
                shape = MaterialTheme.shapes.medium
            )
//------------------------------------------Precio-----------------------------------------
            OutlinedTextField(

                value = precio,
                onValueChange = { nuevoValor ->
                    if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                        precio = nuevoValor
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Costo") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.dinero),
                        contentDescription = "costo"
                    )
                },
                singleLine = true,  // Importante para campos de texto simple
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    textAlign = TextAlign.End  // Alineación derecha para valores numéricos
                ),
                shape = MaterialTheme.shapes.medium,  // Esquinas redondeadas
            )

            //funcion para parte de peso y medida
//------------------------------------------cantidadymedida-----------------------------------------

            Row(
                modifier//----------------cantidad y peso
                    .padding(horizontal = 10.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

//------------------------------------------Cantidad-----------------------------------------


                OutlinedTextField(
                    modifier = modifier
                        .padding(horizontal = 5.dp)
                        .width(180.dp)
                        .heightIn(min = 56.dp),
                    value = cantidad,
                    onValueChange = { nuevoValor ->
                        if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                            cantidad = nuevoValor
                        }
                    },
                    label = { Text("Cant/Peso") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_scale),
                            contentDescription = "Cantidad o Peso"
                        )
                    }, shape = MaterialTheme.shapes.medium
                )
//------------------------------------------medida-----------------------------------------

                ExposedDropdownMenuBox(//menu desplegable para medida
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(

                        value = unidad,
                        onValueChange = { },
                        label = { Text("Medida") },
                        readOnly = true,
                        modifier = modifier
                            .width(100.dp)
                            .heightIn(min = 56.dp)
                            .menuAnchor(MenuAnchorType.PrimaryEditable, true),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        shape = MaterialTheme.shapes.medium
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        medidaList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    unidad = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            }
//------------------------------------------Nota-----------------------------------------

            OutlinedTextField(//nota por si le pica el culo al usuario
                value = nota,
                onValueChange = { nota = it },
                label = { Text("Nota") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_editnote),
                        contentDescription = "Nota"
                    )
                },
                        shape = MaterialTheme.shapes.medium

            )
        }
        if (dialogErrorAlert) DialogoError { dialogErrorAlert = false }


    }

}
//------------------------------------------Alertdialog-----------------------------------------

@Composable
fun DialogoError(onDismiss: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        content = {
            Text(
                text = "Debe colocar un nombre para guardar",
                modifier = Modifier
                    .fillMaxSize(),
                textAlign = TextAlign.Center,
            )
        })
}

//------------------------------------------bottombar-----------------------------------------
@Composable
fun BottombarCreateFood() {
    BottomAppBar(containerColor = Orange) { }

}

//------------------------------------------topbar-----------------------------------------

@Composable
fun TopMenuCreateFood(
    modifier: Modifier = Modifier,
    navigateToback: () -> Unit,
    saveProduct: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        title = {
            Text(text = "CREACION DE PRODUCTO", fontWeight = FontWeight.Bold)

        },
        navigationIcon = {

            IconButton(onClick = { navigateToback() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

        },
        actions = {
            IconButton(onClick = { saveProduct() }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Listo"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarColors(
            containerColor = Orange,
            scrolledContainerColor = Orange,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Unspecified

        )
    )

}
//------------------------------------------Preview-----------------------------------------

@Preview
@Composable
fun CreateFoodView() {
    //CreateFoodScreen {}

}