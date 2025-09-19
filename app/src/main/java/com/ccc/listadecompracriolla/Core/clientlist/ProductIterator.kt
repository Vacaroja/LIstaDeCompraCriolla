package com.ccc.listadecompracriolla.Core.clientlist

import android.icu.text.DecimalFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.Product
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import com.ccc.listadecompracriolla.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProducIterator(
    product: Product,
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel,
    selected: Boolean,
    onChangeProduct: (Int?) -> Unit,
    onSelected: (Int?) -> Unit
) {
//------------------------------------------variables-----------------------------------------

    var precio by remember { mutableStateOf("") }//var price
    var cantidad by remember { mutableFloatStateOf(product.cant) }//var cant
    var showBottomSheet by remember { mutableStateOf(false) }//var of BottomScreen
    val focusManager = LocalFocusManager.current
    val tasa by viewModel.tasa.collectAsState()


    //-------------------------------------textParameters-------------------------------------
    val df = DecimalFormat("#.##")
    val maxLinesName = 3
    val fontSizeName = 15.sp
    val fontSizePrice = 12.sp

    val fontSizeCost = 90.dp


    //-------------------------------------paddingValues-----------------------------

    val namePaddingValues = 90.dp
//------------------------------------------variables de estado-----------------------------------------
    val isSelected = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary

    Card(
        modifier
            .fillMaxSize()
            .padding(5.dp)
            .combinedClickable(onClick = {
                if (viewModel.isNotEmptyProductSelected()) onSelected(product.id) else onChangeProduct(product.id)
            }, onLongClick = {
                onSelected(product.id)
            }),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer),
        colors = CardDefaults.cardColors(
            containerColor = isSelected,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
//------------------------------------------NameButton-----------------------------------------

            Checkbox(
                onCheckedChange = { //verificar status de checkbox
                    viewModel.toggleCheck(product.id)
                },//cambiar valor de producto para ver cuales productos estan en carrito
                checked = product.checked,
                )


            Text(
                text = product.name,
                textDecoration = if (product.checked) TextDecoration.LineThrough else TextDecoration.None,
                fontSize = fontSizeName,
                modifier = modifier.widthIn(max = namePaddingValues, min = namePaddingValues),
                maxLines = maxLinesName
            )

//------------------------------------------cantidad-----------------------------------------


            Row(modifier = modifier.padding(horizontal = 5.dp)) {

                Text(
                    text = "$cantidad ${product.medida}",
                    modifier = modifier.widthIn(max = 60.dp),
                )

            }
//------------------------------------------Price*Cant-----------------------------------------

            if (product.price != 0f) {
                TextButton(
                    onClick = { showBottomSheet = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text(
                        text = "$: ${
                            formatNumber(
                                df.format(product.price * product.cant * tasa).toFloat()
                            )
                        }",
                        modifier = modifier.widthIn(max = fontSizeCost, min = fontSizeCost),
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSizePrice,

                        )
                }
            } else {
                IconButton(
                    onClick = { showBottomSheet = true },
                    modifier = modifier.widthIn(max = fontSizeCost, min = fontSizeCost)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.changeprice),
                        contentDescription = "cambiar precio",
                    )
                }
            }
//------------------------------------------BottomSheettToChangePrice-----------------------------------------


        }
    }
    if (showBottomSheet) {
        SheetToChangePrice(
            precio = precio,
            onDismiss = { showBottomSheet = false },
            onSave = {
                focusManager.clearFocus()
                showBottomSheet = false
                try {
                    viewModel.updatePrecio(product.id, precio.toFloat())
                } catch (_: Exception) {
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
