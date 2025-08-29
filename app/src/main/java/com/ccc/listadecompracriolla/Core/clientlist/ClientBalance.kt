package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Red
import com.ccc.listadecompracriolla.ui.theme.greenApple

@Composable
fun ClientBalance(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    stateOfBalance: Boolean,
    onDimiss: () -> Unit
) {
    val actualPresu by viewModel.actualList.collectAsState()
    val presupuesto by viewModel.presupuesto.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val tasa by viewModel.tasa.collectAsState()
    val focusElements = remember { FocusRequester() }
    var deathPresu by remember { mutableStateOf(false) }
    AnimatedContent(
        targetState = stateOfBalance,
        modifier = modifier.padding(all = 5.dp)
    ) { isExpanded ->
        if (isExpanded) {
            Row {

                OutlinedTextField(
                    modifier = modifier
                        .width(150.dp)
                        .heightIn(min = 56.dp)
                        .focusRequester(focusElements),
                    value = presupuesto,
                    onValueChange = { nuevoValor ->
                        if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                            viewModel.addPresu(actualPresu.id, nuevoValor)
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),

                    keyboardActions = KeyboardActions(onDone = {
                        onDimiss()
                    })
                )
                LaunchedEffect(Unit) { focusElements.requestFocus() }
            }
        } else {
            Card(colors = CardDefaults.cardColors(if (deathPresu) Red else greenApple)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onDimiss() }

                    ) {
                        if (presupuesto.isEmpty()) {
                            Icon(
                                painter = painterResource(id = R.drawable.addcard),
                                contentDescription = "Presupuesto",
                                modifier = Modifier.size(30.dp)
                            )
                        } else {
                            if (deathPresu) {
                                Icon(
                                    painter = painterResource(id = R.drawable.offcard),
                                    contentDescription = "Presupuesto",
                                    modifier = Modifier.size(30.dp)
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.okcard),
                                    contentDescription = "Presupuesto",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }


                    }
                    if (presupuesto.isNotEmpty() && presupuesto != ".") {
                        deathPresu = if (presupuesto.toFloat() < inCar) true else false
                        TextButton(onClick = { onDimiss() }) {
                            Text(
                                text = (formatNumber((presupuesto.toFloat() * tasa) - (inCar * tasa))),
                                fontSize = 16.sp,
                                maxLines = 1
                            )
                        }

                    }
                }

            }
        }
    }
}
