@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core
/*
Terminar boton presupuesto
retornar producto de CreateFoodScreen
Colocar Scroll
colocar animaciones
orden alfabetico
filtrar por orden  y colocar las ya seleccionadas abajo
volver clickeable las cards y textbutton
 viewModel.addProduct(
                        producto = Product(
                            1,
                            "Leche",
                            1f,
                            5.5f
                        )) */

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility

import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange
import java.text.NumberFormat
import java.util.Locale

//Product(1, "Leche", 1f, 5.5f)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientListScreen(
    modifier: Modifier = Modifier,
    navigateToCreateFood: () -> Unit,
    navigateToback: () -> Unit,
    viewModel: ProductViewModel
) {
//------------------------------------------Variables-----------------------------------------

    val scrollState = rememberLazyListState()
    val isExtended by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }
    val productos by viewModel.productos.collectAsState()
    var stateOfBalance by remember { mutableStateOf(false) }
    var presupuesto by remember { mutableStateOf("") }
    //variables para enfocar
    val focusManager = LocalFocusManager.current
    val focusElements = remember { FocusRequester() }
//------------------------------------------Variables-----------------------------------------


    Scaffold(
        topBar = { TopMenu(viewModel = viewModel, navigateToback = { navigateToback() }) },
        bottomBar = { BottomClientList(viewModel = viewModel) },
        floatingActionButton = {//animacion del floatingActionButton
            AnimatedVisibility(
                visible = isExtended,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
//------------------------------------------FBA-----------------------------------------

                ExtendedFloatingActionButton(
                    onClick = { navigateToCreateFood() },
                    icon = { Icon(Icons.Default.Add, "Agregar producto") },
                    text = { Text("AÑADIR") }, // Texto visible solo cuando no hay scroll
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (!isExtended) {
                FloatingActionButton(
                    onClick = { /* Misma acción */ },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, "Agregar PRODUCTO")
                }
            }
        }
    ) { innerpadding ->
//------------------------------------------iterationOfProducts-----------------------------------------
        Box(modifier = modifier.clickable { focusManager.clearFocus();stateOfBalance = false }) {

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerpadding),
                state = scrollState
            ) {
                println(productos)//reciclerview to watch items or products
                items(
                    items = productos,
                    key = {it.id}) { producto ->
                    ProducIterator(
                        product = producto,
                        viewModel = viewModel,

                        )
                }
            }
            Row(
                modifier = modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 120.dp, start = 5.dp)
                    .zIndex(1f) // Asegura que el botón esté por encima
            ) {

                AnimatedContent(targetState = stateOfBalance) { isExpanded ->
                    if (isExpanded) {
                        Row(
                        ) {

                            OutlinedTextField(
                                modifier = modifier
                                    .width(180.dp)
                                    .heightIn(min = 56.dp),//.focusRequester(focusElements),
                                value = presupuesto,
                                onValueChange = { nuevoValor ->
                                    if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                                        presupuesto = nuevoValor
                                    }
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_balance),
                                        contentDescription = "Presupuesto"
                                    )
                                },
                                label = { Text("Presupuesto") },
                                shape = MaterialTheme.shapes.large,
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    textAlign = TextAlign.End  // Alineación derecha para valores numéricos
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    } else {
                        Card(colors = CardDefaults.cardColors(Orange)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { stateOfBalance = !stateOfBalance }

                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.account_balance),
                                        contentDescription = "Presupuesto",
                                        modifier = Modifier.size(40.dp)
                                    )


                                }
                                if (!presupuesto.isEmpty()) {
                                    TextButton(onClick = { stateOfBalance = !stateOfBalance }) {
                                        Text(
                                            text = (formatNumber(presupuesto.toFloat())),
                                            fontSize = 20.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }

                    }
                }

            }
        }
    }
}

//----------------------------------------------TopmenuClientList----------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    navigateToback: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "LISTA DE PRODUCTOS",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
//------------------------------------------NavigationIcons-----------------------------------------

        }, navigationIcon = {
            IconButton(onClick = { navigateToback() }) {//BOTON PARA IR HACIA ATRAS
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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


}

@Composable
fun BottomClientList(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------Variables-----------------------------------------

    val total by viewModel.total.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val tasa by viewModel.tasa.collectAsState()
//------------------------------------------Variables-----------------------------------------


    BottomAppBar(containerColor = Orange) {
//------------------------------------------BottomPresupuesto-----------------------------------------
//------------------------------------------animacion que revisar y ver si borrar-----------------------------------------


//------------------------------------------BottomInCarPrice-----------------------------------------
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Productos en carrito",
            modifier
                .size(40.dp)
                .padding(start = 5.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "En carrito",
                fontWeight = FontWeight.Bold
            )
            Text(text = "${inCar * tasa}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }

        Spacer(modifier.weight(1f))

        VerticalDivider(
            color = Color.Black, thickness = 2.dp,
            modifier = modifier.padding(
                vertical = 7.dp, horizontal = 5.dp
            )
        )
//------------------------------------------BottomTotalPrice-----------------------------------------
        Spacer(modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.dinero),
            contentDescription = "",
            modifier.size(40.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${total * tasa}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
}
//------------------------------------------ProducIterator-----------------------------------------

@Composable
fun ProducIterator(product: Product, modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------variables-----------------------------------------

    var precio by remember { mutableStateOf("") }//var price
    var cantidad by remember { mutableFloatStateOf(product.cant) }//var cant
    var checked by remember { mutableStateOf(false) }//var of checkBox
    var showBottomSheet by remember { mutableStateOf(false) }//var of BottomScreen
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val tasa by viewModel.tasa.collectAsState()
//------------------------------------------variables de estado-----------------------------------------
    println("patata${product.id} name:${product.name}")
    Card(
        modifier
            .fillMaxSize()
            .padding(2.dp)
            .border(border = BorderStroke(1.dp, Color.Red), shape = RoundedCornerShape(16.dp))
            .padding(10.dp),
        colors = CardDefaults.cardColors()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
//------------------------------------------NameButton-----------------------------------------

            Checkbox(
                onCheckedChange = {
                    checked = it//verificar status de checkbox
                    viewModel.toggleCheck(product.id)
                },//cambiar valor de producto para ver cuales productos estan en carrito
                checked = checked
            )
            if (!checked) {
                TextButton(
                    onClick = {},

                    )
                { Text(product.name, color = Color.Blue, fontSize = 20.sp) }
            } else {
                TextButton(
                    onClick = {},

                    ) {
                    Text(
                        product.name, color = Color.Blue,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 20.sp
                    )
                }
            }
//------------------------------------------añadir y disminuir cantidad-----------------------------------------

            //convertir en Textbutton para cambiarlo directamente
            Spacer(modifier = Modifier.weight(1f)) //espaciado de los botones
            IconButton(onClick = { cantidad += 1;viewModel.updateCantidad(product.id, cantidad) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plus"
                )
            }
            Text(text = "$cantidad")
            IconButton(onClick = {
                if (cantidad > 0) cantidad -= 1 else cantidad
                viewModel.updateCantidad(product.id, cantidad)
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_minus),
                    contentDescription = "minus"
                )
            }
//------------------------------------------buttonBottomSheettToChangePrice-----------------------------------------

            TextButton(onClick = { showBottomSheet = true }) {
                Text(
                    text = "Costo: ${product.price * product.cant * tasa}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
//------------------------------------------BottomSheettToChangePrice-----------------------------------------

                if (showBottomSheet) {
                    ModalBottomSheet(
                        modifier = Modifier.fillMaxHeight(),
                        sheetState = sheetState,
                        onDismissRequest = { showBottomSheet = false }
                    ) {
                        Column(modifier.padding(15.dp), verticalArrangement = Arrangement.Center) {
                            Text(text = "Coloque el precio que quierer cambiar", fontSize = 20.sp)
                            Row {
                                OutlinedTextField(
                                    modifier = modifier
                                        .width(180.dp)
                                        .heightIn(min = 56.dp),
                                    value = precio,
                                    onValueChange = { nuevoValor ->
                                        if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                                            precio = nuevoValor
                                        }
                                    },
                                    label = { Text("precio") },
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )
                                Spacer(modifier.weight(1f))
                                Button(modifier = modifier.width(120.dp),onClick = {
                                    product.price = precio.toFloat()
                                }) { Text("cambiar") }
                            }
                        }
                    }
                }

            }

        }
    }
}
//------------------------------------------Preview-----------------------------------------

/*Preview
@Composable
fun Viewer() {
    ClientListScreen(navigateToback = {}, navigateToCreateFood = {})
}*/

fun formatNumber(number: Float): String {
    // Puedes especificar la configuración regional (Locale) aquí.
    // Locale.US usa comas para miles y puntos para decimales (ej. 1,234.56).
    // Locale.getDefault() usa la configuración regional del dispositivo.
    val formatter = NumberFormat.getNumberInstance(Locale.US)

    // Si quisieras que el formato usara puntos para miles y comas para decimales
    // (común en muchos países de Europa y América Latina), usarías una Locale diferente,
    // por ejemplo, Locale("es", "ES") para España o Locale("es", "VE") para Venezuela.
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "ES")) // Ejemplo para España
    // val formatter = NumberFormat.getNumberInstance(Locale("es", "VE")) // Ejemplo para Venezuela

    return formatter.format(number)
}



