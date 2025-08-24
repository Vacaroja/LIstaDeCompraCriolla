package com.ccc.listadecompracriolla.Core.clientlist

import android.icu.text.DecimalFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R

@Composable
fun ProducIterator(product: Product, modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------variables-----------------------------------------

    var precio by remember { mutableStateOf("") }//var price
    var cantidad by remember { mutableFloatStateOf(product.cant) }//var cant
    var checked by remember { mutableStateOf(false) }//var of checkBox
    var showBottomSheet by remember { mutableStateOf(false) }//var of BottomScreen
    val focusManager = LocalFocusManager.current
    val tasa by viewModel.tasa.collectAsState()
    val df = DecimalFormat("#.##")
    val maxLinesName = 3
    val fontSizeName = 15.sp
    val fontSizePrice = 12.sp

//------------------------------------------variables de estado-----------------------------------------

    Card(
        modifier
            .fillMaxSize()
            .padding(5.dp).clickable{},
        border = BorderStroke(1.dp,Color.Black),
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
                Text(
                    product.name,
                    color = Color.Blue,
                    fontSize = fontSizeName,
                    modifier = modifier.widthIn(max = 90.dp),
                    maxLines = maxLinesName
                )
            } else {
                    Text(
                        product.name, color = Color.Blue,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = fontSizeName,
                        modifier = modifier.widthIn(max = 90.dp),
                        maxLines = maxLinesName
                    )

            }

//------------------------------------------añadir y disminuir cantidad-----------------------------------------

            //convertir en Textbutton para cambiarlo directamente


            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    cantidad += 1;viewModel.updateCantidad(
                    product.id,
                    cantidad
                )
                }) {
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
            }

            TextButton(onClick = { showBottomSheet = true }) {
                Text(
                    text = if (product.price != 0f) "$: ${df.format(product.price * product.cant * tasa)}" else "$",
                    fontWeight = FontWeight.Bold,
                    fontSize = fontSizePrice

                )
            }

//------------------------------------------Price*Cant-----------------------------------------


//------------------------------------------BottomSheettToChangePrice-----------------------------------------
            if (showBottomSheet) {
                SheetToChangePrice(
                    precio = precio,
                    onDismiss = { showBottomSheet = false },
                    onSave = {
                        focusManager.clearFocus()
                        showBottomSheet = false
                        try {
                            viewModel.updatePrecio(product.id, precio.toFloat())
                        }catch (_: Exception){
                            viewModel.updatePrecio(product.id, 0f)
                        }
                        precio = ""
                    },
                    onChange = { nuevoValor ->
                        if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                            precio = nuevoValor
                        }
                    })


            }

        }
    }
}