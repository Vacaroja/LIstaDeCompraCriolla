@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core
/*Cosas por hacer
//cambiar tipo de teclado en selector de cantidad y precio
//cambiar por lista desplegable la medida
arreglar error con el .
 */

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange
import kotlinx.coroutines.launch

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
    val actual by viewModel.actualList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val focusElements = remember { FocusRequester() }

    //-----------------------------TextFieldParameter--------------------------

    val textFieldMediumWidth = 180.dp
    val textFieldMediumHeight = 56.dp


    LaunchedEffect(Unit) { focusElements.requestFocus()}

    Scaffold(
//------------------------------------------topbar-----------------------------------------
        topBar = {
            TopMenuCreateFood(
                navigateToback = { navigateToback() },
                saveProduct = {
                    if (nombre.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Debe colocar un nombre",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                        }
                    } else {
                        val nuevoProducto = Product(
                            id = productos.size + 1, // O usa un UUID
                            name = nombre,
                            cant = if (cantidad.isNotEmpty() && cantidad != ".") cantidad.toFloat() else 1f,
                            price = if (precio.isNotEmpty() && precio != ".") precio.toFloat() else 0f,
                            nota = nota,
                            medida = unidad,
                            client = actual.id
                        )
                        viewModel.addProduct(nuevoProducto)
                        navigateToback()
                        // Opcional: regresar después de guardar

                    }

                }
            )
        },
//------------------------------------------bottombar-----------------------------------------

        bottomBar = { BottombarCreateFood() },
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}//bottom bar por si quiero colocar una mariquera
    )
//------------------------------------------initContent-----------------------------------------

    { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize().clickable{focusManager.clearFocus()},
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
                shape = MaterialTheme.shapes.medium,
                modifier = modifier.focusRequester(focusElements),
                maxLines = 3
            )
            Row{
//------------------------------------------Precio-----------------------------------------
            OutlinedTextField(
                modifier = modifier.width(textFieldMediumWidth)
                    .heightIn(min = textFieldMediumHeight),

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
                Card(modifier = modifier.padding(5.dp)) {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.bolivar),contentDescription = "bolivares")
                    }
                }
                Card(modifier = modifier.padding(5.dp)) {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.dolar),contentDescription = "bolivares")
                    }
                }
            }

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
                        .width(textFieldMediumWidth)
                        .heightIn(min = textFieldMediumHeight),
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

