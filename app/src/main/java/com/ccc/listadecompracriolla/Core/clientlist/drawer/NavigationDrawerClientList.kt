package com.ccc.listadecompracriolla.Core.clientlist.drawer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.ui.theme.Blue
import com.ccc.listadecompracriolla.ui.theme.Red
import com.ccc.listadecompracriolla.ui.theme.amarillo
import com.ccc.listadecompracriolla.ui.theme.oro

@Composable
fun NavigationDrawerClientList(
    modifier: Modifier = Modifier,
) {
    val titleDrawerBrush = Brush.horizontalGradient(listOf(amarillo, Blue, Red))
    val titleDrawerPaddingValues =
        PaddingValues(start = 15.dp, top = 30.dp, end = 5.dp, bottom = 5.dp)

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
        NavigationDrawerItem(//pantalla de ajustes
            label = { Text("Ajustes") },
            selected = false,
            onClick = { /* Handle click */ },
            icon = { Icon(imageVector = Icons.Default.Settings, "ajustes") }
        )
        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Premiun", color = oro) },
            selected = false,
            onClick = { /* Handle click */ },
            icon = { Icon(imageVector = Icons.Default.Star, "Premiun", tint = oro) }
        )
        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Apoyanos ", color = Red) },
            selected = false,
            onClick = { /* Handle click */ },
            icon = { Icon(imageVector = Icons.Default.Favorite, "Apoyanos", tint = Red) }
        )
        NavigationDrawerItem(//pantalla de acerca de la aplicacion
            label = { Text("Info de app") },
            selected = false,
            onClick = { /* Handle click */ },
            icon = { Icon(imageVector = Icons.Default.Info, "info") }
        )
    }
}