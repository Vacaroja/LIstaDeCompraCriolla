package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

//------------------------------------------variables de estado-----------------------------------------

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
                    SheetToChangePrice(
                        precio = precio,
                        onDismiss = { showBottomSheet = false },
                        onSave = {
                            focusManager.clearFocus()
                            showBottomSheet = false
                            viewModel.updatePrecio(product.id, precio.toFloat())
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
}