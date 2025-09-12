@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core.createfood
/*Cosas por hacer
//cambiar tipo de teclado en selector de cantidad y precio
//cambiar por lista desplegable la medida
arreglar error con el .
 */

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.adds.AdBanner
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToback: () -> Unit,
) {
//------------------------------------------variables de estado-----------------------------------------
    val actProduct by viewModel.actualprod.collectAsState()
    val tasa by viewModel.tasa.collectAsState()
    val actual by viewModel.actualList.collectAsState()

    val medidaList = listOf("Und", "Lb", "Kg", "L")


    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf(medidaList[0]) }
    var cantidad by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }


    val isPressed by viewModel.isBcv.collectAsState()

    var enableButton by remember { mutableStateOf(true) }



    if (actProduct.id != 0) {
        nombre = actProduct.name
        precio = actProduct.price.toString()
        nota = actProduct.nota
        unidad = actProduct.medida
        cantidad = actProduct.cant.toString()
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val focusElements = remember { FocusRequester() }

    //-----------------------------TextFieldParameter--------------------------

    val textFieldMediumWidth = 180.dp
    val textFieldMediumHeight = 56.dp




    LaunchedEffect(Unit) { focusElements.requestFocus() }

    BackHandler(enabled = true) {
        viewModel.actualizeProduct(-1)
        navigateToback()
    }

    Scaffold(
//------------------------------------------topbar-----------------------------------------
        topBar = {
            TopMenuCreateFood(
                enableButton = enableButton,
                navigateToback = {
                    enableButton = false
                    viewModel.actualizeProduct(-1)
                    navigateToback()
                },
                saveProduct = {
                    if (nombre.isBlank()) {
                        focusManager.clearFocus()
                        scope.launch {
                            val vacio = snackbarHostState.showSnackbar(
                                message = "Debe colocar un nombre",
                                actionLabel = "OK",
                                duration = SnackbarDuration.Short
                            )
                            when (vacio) {
                                SnackbarResult.Dismissed -> {}
                                SnackbarResult.ActionPerformed -> {
                                    focusElements.requestFocus()
                                }
                            }
                        }
                    } else {
                        enableButton = false
                        val nuevoProducto = Product(
                            id = if (actProduct.id != 0) actProduct.id else null, // O usa un UUID
                            name = nombre,
                            cant = if (cantidad.isNotEmpty() && cantidad != ".") cantidad.toFloat() else 1f,
                            price = if (precio.isNotEmpty() && precio != ".") (if (isPressed) (precio.toFloat() / tasa) else precio.toFloat()) else 0f,
                            nota = nota,
                            medida = unidad,
                            client = if (actProduct.id != 0) actProduct.client else actual.id
                        )
                        if (actProduct.id != 0) {
                            viewModel.updateProduct(nuevoProducto)
                            viewModel.actualizeProduct(-1)
                        } else {
                            viewModel.addProduct(nuevoProducto)
                        }
                        navigateToback()


                    }

                }
            )
        },
//------------------------------------------bottombar-----------------------------------------

        bottomBar = { BottombarCreateFood() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    )
//------------------------------------------initContent-----------------------------------------

    { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clickable { focusManager.clearFocus() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//------------------------------------------nombre-----------------------------------------


            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("NOMBRE") },
                placeholder = { Text("Coloque el nombre") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Nombre"
                    )
                },
                shape = MaterialTheme.shapes.medium,
                modifier = modifier.focusRequester(focusElements),
                maxLines = 3
            )

            PriceCreateFood(
                textFieldMediumWidth = textFieldMediumWidth,
                textFieldMediumHeight = textFieldMediumHeight,
                precio = precio,
                isPressed = isPressed,
                newPrice = { nuevoValor -> precio = nuevoValor },
                onBsPrice = { viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BCV) },
                onDollarPrice = { viewModel.actualizarTasa() },
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
                DropdownMenuCreateFood(
                    unidad = unidad,
                    expanded = expanded,
                    medidaList = medidaList,
                    onExpandex = { expanded = !expanded },
                    onChangeUnidad = { newUnidad ->
                        unidad = newUnidad
                    }
                )

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
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                AdBanner()
            }

        }


    }

}



