package com.ccc.listadecompracriolla.Core.clientlist.top

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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClientList(
    lastName : String?,
    modifier:Modifier = Modifier,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
    onChangeName: (String) -> Unit,
){
    val focusElements = remember { FocusRequester() }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var nameClientList by remember { mutableStateOf("") }


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
            Text(text = "Coloque el nombre de la lista", fontSize = 20.sp)
            Row {
                OutlinedTextField(
                    modifier = modifier
                        .width(180.dp)//modificador de anchura
                        .heightIn(min = 56.dp) // modificador de altura
                        .focusRequester(focusElements),// focus para sacar el teclado
                    value = nameClientList,
                    onValueChange = {
                        nameClientList = it
                    },
                    placeholder = {if (lastName != null) Text(lastName)},
                    singleLine = true,
                    label = { Text("Lista") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (nameClientList.isNotEmpty()) {
                            if (lastName != null) {onChangeName(nameClientList)}
                            else onSave(nameClientList)}
                    })
                )
                LaunchedEffect(Unit) { focusElements.requestFocus() }

            }
        }
    }
}