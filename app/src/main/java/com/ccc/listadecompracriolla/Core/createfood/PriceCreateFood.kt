package com.ccc.listadecompracriolla.Core.createfood

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.animation.InfiniteAnimationIconRates
import com.ccc.listadecompracriolla.ui.buttoncolor.animateColorButtonClientList
import com.ccc.listadecompracriolla.ui.theme.greenApple
import com.ccc.listadecompracriolla.ui.theme.pressedColorButton

@Composable
fun PriceCreateFood(
    textFieldMediumWidth: Dp,
    textFieldMediumHeight: Dp,
    modifier: Modifier = Modifier,
    precio: String,
    isPressed: Int,
    validTasa: Boolean,
    newPrice: (String) -> Unit,
    onBsPrice: () -> Unit,
    onDollarPrice: () -> Unit,
    onEurPrice: () -> Unit,
    onCantRequest: () -> Unit

) {
    val animatedColorDollar by animateColorButtonClientList(isPressed,primaryColor = greenApple, secondaryColor = pressedColorButton)
    val animatedColorBs by animateColorButtonClientList(isPressed,2,primaryColor = greenApple, secondaryColor = pressedColorButton)
    val animatedColorEur by animateColorButtonClientList(isPressed,3,primaryColor = greenApple, secondaryColor = pressedColorButton)
    val elevateLevel :(Int)-> Dp = { selected ->
        if (isPressed == selected) 15.dp
        else 2.dp
    }
    Row {
        OutlinedTextField(
            modifier = modifier
                .width(textFieldMediumWidth)
                .heightIn(min = textFieldMediumHeight),
            value = precio,
            onValueChange = { nuevoValor ->
                if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                    newPrice(nuevoValor)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                onCantRequest()
            }),
            label = { Text("Costo") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.dinero),
                    contentDescription = "Precio"
                )
            },
            singleLine = true,  // Importante para campos de texto simple
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.End  // Alineación derecha para valores numéricos
            ),
            shape = MaterialTheme.shapes.medium,  // Esquinas redondeadas
        )

        //------------------------------------ USD -------------------------------------------
        if (!validTasa) {
            Card(
                modifier = modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .clickable {
                        onDollarPrice()
                    },
                elevation = CardDefaults.cardElevation(
                    // Cambiar la elevación para simular el hundimiento
                    defaultElevation = elevateLevel(1)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = animatedColorDollar
                )
            ) {
                InfiniteAnimationIconRates(
                    modifier = modifier,
                    secondaryIcon = R.drawable.dolar,
                )

            }
//--------------------------------- BS / USD RATE ----------------------------------------
            Card(
                modifier = modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .clickable {
                        onBsPrice()
                    },
                elevation = CardDefaults.cardElevation(
                    // Cambiar la elevación para simular el hundimiento
                    defaultElevation = elevateLevel(2)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = animatedColorBs
                )
            ) {
                InfiniteAnimationIconRates(
                    modifier = modifier,
                    secondaryIcon = R.drawable.bs_icono,
                    primaryIcon = R.drawable.bsdollar,
                )
            }
//------------------------------------------- BS / Eur Rate ---------------------------------------------
            Card(
                modifier = modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .clickable {
                        onEurPrice()
                    },
                elevation = CardDefaults.cardElevation(
                    // Cambiar la elevación para simular el hundimiento
                    defaultElevation = elevateLevel(3)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = animatedColorEur
                )
            ) {
                InfiniteAnimationIconRates(
                    modifier = modifier,
                    secondaryIcon = R.drawable.euro_symbol,
                    primaryIcon = R.drawable.bseur,
                )

            }
        }

    }
}
