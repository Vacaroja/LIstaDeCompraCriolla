package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogIsWeekend(onDismiss:()-> Unit,onConfirm:() -> Unit){
    AlertDialog(
        confirmButton = { TextButton(onClick = {onConfirm()}){Text("Acepto")} },
        onDismissRequest = {onDismiss()},

        dismissButton = {TextButton(onClick = {onDismiss()}){Text("Declino")}},
        title = { Text("Precaucion", style = MaterialTheme.typography.titleMedium) },
        text = {Text("Algunos comercios usan la tasa del proximo dia habil\n ¿Desea cargar esa tasa?")},
    )
}