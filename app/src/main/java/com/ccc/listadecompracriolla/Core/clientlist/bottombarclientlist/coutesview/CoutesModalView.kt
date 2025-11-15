package com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist.coutesview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.clases.coutes.funtioncoutes
import com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist.ClientBalance
import com.ccc.listadecompracriolla.Core.clientlist.principal.ratesbuttons.RateButton
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoutesModal(
    modifier: Modifier = Modifier,
    total: Float = 1f,
    viewModel: ProductViewModel,
    isPressed: Int,
    onDismiss: () -> Unit
) {
    // valores iniciales
    var percent by remember { mutableFloatStateOf(1f) }
    val cuotes = listOf(1, 2, 3, 6, 9, 12)
    // valores para seleccionar
    var selectedPercent by remember { mutableFloatStateOf(1f) }
    var selectedCuotes by remember { mutableIntStateOf(cuotes.first()) }
    //inicializacion de clase para coutes
    val inicial = funtioncoutes()
    //valor de sheet state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val moneySymbol by viewModel.moneySymbol.collectAsState()

    val fontSizeCuotes = 30.sp




    ModalBottomSheet(onDismissRequest = { onDismiss() }, sheetState = sheetState) {
        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Simulador de Financiamiento", style = MaterialTheme.typography.titleLarge)
            Text("Seleccione el porcentaje de inicial")
            if (selectedPercent != 1f) Text("${formatNumber(percent)}%", fontSize = fontSizeCuotes)
            Slider(value = percent, onValueChange = {
                percent = it
                selectedPercent = percent / 100
            }, valueRange = 20f..70f, steps = 9, modifier = modifier.padding(horizontal = 10.dp))
            LazyRow {
                item {
                    Card {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = modifier.padding(10.dp)
                        ) {
                            Text("Su Total en$moneySymbol es:", fontWeight = FontWeight.Bold)

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatNumber(total),
                                    textAlign = TextAlign.Center,
                                    fontSize = fontSizeCuotes,
                                )

                            }

                        }
                    }
                }
            }
            Text("Seleccione cantidad de cuotas", modifier = modifier.padding(5.dp))

            Text("Cuotas: $selectedCuotes", fontSize = fontSizeCuotes)

            Slider(
                value = cuotes.indexOf(selectedCuotes).toFloat(),
                valueRange = 0f..(cuotes.size - 1).toFloat(), steps = (cuotes.size - 1),
                onValueChange = { indexFloat ->
                    val indexInt = indexFloat.roundToInt()

                    selectedCuotes = cuotes[indexInt]
                }, modifier = modifier.padding(horizontal = 10.dp)
            )
            AnimatedVisibility(visible = selectedPercent != 1f) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LazyRow(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item {
                            Card(modifier = modifier.padding(10.dp)) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = modifier.padding(10.dp)
                                ) {
                                    Text(
                                        "Su inicial en$moneySymbol es:",
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (selectedPercent != 1f) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = formatNumber(
                                                    inicial.firstCoute(
                                                        total,
                                                        selectedPercent
                                                    )
                                                ),
                                                textAlign = TextAlign.Center,
                                                fontSize = fontSizeCuotes,

                                                )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = modifier
                                    .padding(5.dp)
                            ) {
                                Column(
                                    modifier = modifier.padding(5.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        "Queda pagando ${selectedCuotes}\n cuotas en$moneySymbol de",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Text(
                                            formatNumber(
                                                inicial.createCoutes(
                                                    total,
                                                    selectedCuotes,
                                                    selectedPercent
                                                )
                                            ),
                                            fontSize = 30.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        //verificar si se cargo la tasa
                        if (!viewModel.validTasa()) {
                            RateButton(
                                viewModel = viewModel,
                                isPressed = isPressed,
                                onFailureApi = {},
                                showActualRate = {}
                            )
                        }
                    }
                }


            }
        }

    }
}