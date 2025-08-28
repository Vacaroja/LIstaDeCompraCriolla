package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetToChangePrice(
    modifier: Modifier = Modifier,
    precio: String,
    onSave: () -> Unit,
    onChange: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val focusElements = remember { FocusRequester() }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {

        Column(
            modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Coloque el precio que quiere cambiar", fontSize = 20.sp)
            Row {
                OutlinedTextField(
                    modifier = modifier
                        .width(180.dp)//modificador de anchura
                        .heightIn(min = 56.dp) // modificador de altura
                        .focusRequester(focusElements),// focus para sacar el teclado
                    value = precio,
                    onValueChange = { nuevoValor ->
                        onChange(nuevoValor)
                    },
                    label = { Text("precio") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.End  // Alineación derecha para valores numéricos
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onSave()
                    })
                )
                LaunchedEffect(Unit) { focusElements.requestFocus() }

            }
        }
    }
}