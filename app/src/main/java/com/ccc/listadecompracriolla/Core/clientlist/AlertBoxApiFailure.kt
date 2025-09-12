package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertBoxDeleteClient(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) { Text("SI") }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) { Text("NO") }


        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alerta de borrado",
                    modifier
                        .size(80.dp)
                        .padding(start = 5.dp),
                    tint = Red
                )
            }
        },
        text = { Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = modifier.fillMaxWidth()) { Text("¿ESTA SEGURO DE QUERER BORRAR LA LISTA?") } })

}