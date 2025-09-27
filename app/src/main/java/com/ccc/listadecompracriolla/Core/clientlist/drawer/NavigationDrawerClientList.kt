package com.ccc.listadecompracriolla.Core.clientlist.drawer

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.ccc.listadecompracriolla.ui.theme.Blue
import com.ccc.listadecompracriolla.ui.theme.Red
import com.ccc.listadecompracriolla.ui.theme.oro
import com.ccc.listadecompracriolla.ui.theme.titleDrawerBrush

@Composable
fun NavigationDrawerClientList(
    modifier: Modifier = Modifier,
    onAbout:() -> Unit
) {
    val titleDrawerPaddingValues =
        PaddingValues(start = 15.dp, top = 30.dp, end = 5.dp, bottom = 5.dp)
    val context = LocalContext.current
    ModalDrawerSheet(
        modifier = modifier
            .requiredWidth(250.dp)
            .fillMaxHeight()
    ) {
        Text(
            text = "Lista de Compra Criolla",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge.copy(brush = titleDrawerBrush),
            modifier = modifier.padding(titleDrawerPaddingValues),
            fontWeight = FontWeight.ExtraBold,

            )

        HorizontalDivider()

       /* NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Premium") },
            selected = false,
            onClick = { context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/Vacaroja/LIstaDeCompraCriolla".toUri()
                )
            ) },
            icon = { Icon(imageVector = Icons.Default.Star, "Premiun", tint = oro) }
        )*/
        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Calificanos ") },
            selected = false,
            onClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://github.com/Vacaroja/LIstaDeCompraCriolla".toUri()
                    )
                )  },
            icon = { Icon(imageVector = Icons.Default.Favorite, "Calificanos", tint = oro) }
        )
        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Info de app") },
            selected = false,
            onClick = { onAbout() },
            icon = { Icon(imageVector = Icons.Default.Info, "info",tint = Blue) }
        )
        HorizontalDivider()

        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Enviar comentarios") },
            selected = false,
            onClick = { sendFeedback(context) },
            icon = { Icon(imageVector = Icons.AutoMirrored.Filled.Send, "enviar comentarios", tint = Red) }
        )
    }
}