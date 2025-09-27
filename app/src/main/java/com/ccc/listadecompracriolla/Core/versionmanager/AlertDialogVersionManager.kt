package com.ccc.listadecompracriolla.Core.versionmanager

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.ccc.listadecompracriolla.R

@Composable
fun ForcedUpdateDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Actualización requerida") },
        icon = {
            Surface(shape = RoundedCornerShape(10.dp)) {
                Image(
                    painterResource(R.drawable.logo_app),
                    "Icono App"
                )
            }
        },
        text = {
            Text("Para seguir usando esta aplicación, por favor, actualiza a la última versión.")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    try {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://play.google.com/store/apps/details?id=com.ccc.listadecompracriolla".toUri()
                            )
                        )
                    } catch (_: Exception) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "https://play.google.com/apps/testing/com.ccc.listadecompracriolla".toUri()
                            )
                        )
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
                Text("Tal vez luego")
            }
        }
    )
}