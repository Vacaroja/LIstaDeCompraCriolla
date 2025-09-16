@file:OptIn(ExperimentalMaterial3Api::class)

package com.ccc.listadecompracriolla.Core

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.ccc.listadecompracriolla.ui.theme.Orange
import com.ccc.listadecompracriolla.ui.theme.Pink80
import com.ccc.listadecompracriolla.ui.theme.titleDrawerBrush


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigateToClient: () -> Unit) {
//------------------------------------------variables de estado-----------------------------------------
    val bcvLink = "https://www.bcv.org.ve/"
    val context = LocalContext.current
    val about =
        "   Esta aplicacion es netamente referencial, la tasa del dolar utilizada es tomada " +
                "del unico ente regulador del precio de las remesas "



//------------------------------------------Scaffold-----------------------------------------

    Scaffold(
        topBar = { TopBarHome { navigateToClient() } },
    ) { innerpadding ->
        Box(
            modifier
                .fillMaxSize()
                .padding(innerpadding)
        ) {
            ElevatedCard(
                modifier = modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Pink80
                ), content = {
                    Row {
                        Spacer(modifier.weight(1f))
                        Text(
                            text = "Lista de Compra Criolla",
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.titleLarge.copy(brush = titleDrawerBrush),
                            fontWeight = FontWeight.ExtraBold,
                            modifier = modifier.padding(top = 10.dp)
                        )
                        Spacer(modifier.weight(1f))

                    }
                    Text(
                        text = about,
                        fontSize = 20.sp,
                        modifier = modifier.padding(10.dp)
                    )
                    Row {
                        Spacer(modifier.weight(1f))
                        Text(
                            text = bcvLink,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline,
                            modifier = modifier.padding(10.dp).clickable(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, bcvLink.toUri())
                                context.startActivity(intent)
                            }))
                        Spacer(modifier.weight(1f))
                    }


                })
        }
    }
}


//------------------------------------------TopBar-----------------------------------------

@Composable
fun TopBarHome(
    modifier: Modifier = Modifier,
    navigateToClient: () -> Unit
) {

//------------------------------------------HorizontalSheet-----------------------------------------
    TopAppBar(
        title = {
            Text(
                text = "INFORMACION",
                fontWeight = FontWeight.Bold
            )
            modifier.padding(15.dp)
        },
        navigationIcon = {
//------------------------------------------EstadoDelHorizontalSheet-----------------------------------------

            IconButton(onClick = {
                navigateToClient()

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {

//------------------------------------------SettingsButton-----------------------------------------


        }, colors = TopAppBarColors(
            containerColor = Orange,
            scrolledContainerColor = Orange,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Unspecified
        )
    )
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen { }
}
