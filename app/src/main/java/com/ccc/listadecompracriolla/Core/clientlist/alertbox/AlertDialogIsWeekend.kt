package com.ccc.listadecompracriolla.Core.clientlist.alertbox

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AlertDialogIsWeekend(modifier: Modifier = Modifier, onDismiss:()-> Unit, onConfirm:() -> Unit){
    AlertDialog(
        confirmButton = { TextButton(onClick = {onConfirm()}){Text("Acepto", textAlign = TextAlign.Center)} },
        onDismissRequest = {onDismiss()},
        dismissButton = {TextButton(onClick = {onDismiss()}){Text("Declino", textAlign = TextAlign.Center)}},
        title = {Row {
            Spacer(modifier.weight(1f))
            Text("AVISO", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            Spacer(modifier.weight(1f))
        }
                },
        text = {Text("Algunos comercios usan la tasa del proximo dia habil\n ¿Desea cargar esa tasa?", textAlign = TextAlign.Center)},
    )
}