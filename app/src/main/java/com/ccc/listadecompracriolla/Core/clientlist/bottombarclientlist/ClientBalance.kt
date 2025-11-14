package com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Red
import com.ccc.listadecompracriolla.ui.theme.black
import com.ccc.listadecompracriolla.ui.theme.greenApple

//mejorar este pedo
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
    val deathPresu by viewModel.deathPresu.collectAsState()
    val animatedColorpresu by animateColorAsState(
        targetValue = if (deathPresu) Red else greenApple,
        animationSpec = tween(durationMillis = 200)
    )
    val haptic = LocalHapticFeedback.current
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val moneySymbol by viewModel.moneySymbol.collectAsState()
    val iconSymbol by viewModel.iconSymbol.collectAsState()

    LaunchedEffect(deathPresu) {//feedback para cuando se pasa de presupuesto
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }
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
                    value = textFieldValue,
                    onValueChange = { nuevoValor ->
                        if (nuevoValor.text.isEmpty() || nuevoValor.text.matches(Regex("^\\d*\\.?\\d*$"))) {
                            textFieldValue = TextFieldValue(
                                text = nuevoValor.text,
                                // Mueve el cursor al final de lo que se ha escrito
                                selection = TextRange(nuevoValor.text.length)
                            )
                            viewModel.addPresu(actualPresu.id, nuevoValor.text)
                        }
                    },
                    leadingIcon = {

                            Icon(
                                painter = painterResource(id = iconSymbol),
                                contentDescription = "Presupuesto moneda",
                                modifier = Modifier.size(30.dp),
                            )

                    },
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
                    }),
                    placeholder = {
                        Text(
                            if (presupuesto.isNotEmpty()) "${presupuesto.toFloat() * tasa}" else "Presupuesto",
                            fontSize = 13.sp
                        )
                    }
                )
                LaunchedEffect(Unit) { focusElements.requestFocus() }
            }
        } else {
            Card(colors = CardDefaults.cardColors(animatedColorpresu)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            textFieldValue = TextFieldValue("")
                            onDimiss()
                        }
                    ) {

                        Icon(
                            painter = if (presupuesto.isEmpty()) {
                                painterResource(id = R.drawable.addcard)
                            } else {
                                if (deathPresu) painterResource(id = R.drawable.offcard)
                                else painterResource(id = R.drawable.okcard)
                            },
                            contentDescription = "Presupuesto",
                            modifier = Modifier.size(30.dp),
                            tint = black
                        )


                    }
                    if (presupuesto.isNotEmpty() && presupuesto != "0") {
                        TextButton(onClick = {
                            textFieldValue = TextFieldValue("")
                            onDimiss()
                        }) {
                            Text(
                                text = (formatNumber((presupuesto.toFloat() * tasa) - (inCar)) + moneySymbol),
                                fontSize = 16.sp,
                                maxLines = 1,
                                color = black
                            )
                        }

                    }
                }

            }
        }
    }
}
