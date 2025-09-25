package com.ccc.listadecompracriolla.Core.createfood

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.black
import com.ccc.listadecompracriolla.ui.theme.pressedColorButton
import com.ccc.listadecompracriolla.ui.theme.unPressedColorButton

@Composable
fun PriceCreateFood(
    textFieldMediumWidth: Dp,
    textFieldMediumHeight: Dp,
    focusPrice: FocusRequester,
    modifier: Modifier = Modifier,
    precio: String,
    isPressed: Boolean,
    newPrice: (String) -> Unit,
    onBsPrice: () -> Unit,
    onDollarPrice: () -> Unit,
    onCantRequest:()-> Unit

    ) {
    val animatedColorBs by animateColorAsState(
        targetValue = if (!isPressed) pressedColorButton else unPressedColorButton,
        animationSpec = tween(durationMillis = 200)
    )
    val animatedColorDolar by animateColorAsState(
        targetValue = if (isPressed) pressedColorButton else unPressedColorButton,
        animationSpec = tween(durationMillis = 200)
    )


    Row {
        OutlinedTextField(
            modifier = modifier
                .width(textFieldMediumWidth)
                .heightIn(min = textFieldMediumHeight).focusRequester(focusPrice),

            value = precio,
            onValueChange = { nuevoValor ->
                if (nuevoValor.isEmpty() || nuevoValor.matches(Regex("^\\d*\\.?\\d*$"))) {
                    newPrice(nuevoValor)
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
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

        //----------------------------boton pa costo en dolares--------------------------
        Card(
            modifier = modifier
                .padding(start = 5.dp, top = 5.dp)
                .clickable {
                    onDollarPrice()
                },
            elevation = CardDefaults.cardElevation(
                // Cambiar la elevación para simular el hundimiento
                defaultElevation = if (!isPressed) 2.dp else 15.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = animatedColorDolar
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.dolar),
                contentDescription = "bolivares",
                modifier = modifier.size(50.dp),
                tint = black
            )

        }
        //----------------------------boton pa costo en bolivares--------------------------
        Card(
            modifier = modifier
                .padding(start = 5.dp, top = 5.dp)
                .clickable {
                    onBsPrice()

                },
            elevation = CardDefaults.cardElevation(
                // Cambiar la elevación para simular el hundimiento
                defaultElevation = if (isPressed) 2.dp else 15.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = animatedColorBs
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bolivar),
                contentDescription = "bolivares",
                modifier = modifier.size(50.dp),
                tint = black
            )

        }

    }
}
