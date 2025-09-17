package com.ccc.listadecompracriolla.Core.versionmanager

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun ForcedUpdateDialog(onDismiss:()-> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Actualización requerida") },
        text = { Text("Para seguir usando esta aplicación, por favor, actualiza a la última versión.") },
        confirmButton = {
            TextButton(
                onClick = {
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://www.google.com/".toUri()
                            )
                        )
                    } catch (_: Exception) {
                        context.startActivity(Intent(Intent.ACTION_VIEW,
                            "https://www.google.com/".toUri()))
                    }
                }
            ) {
                Text("Actualizar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Lo hago luego")
            }
        }
    )
}