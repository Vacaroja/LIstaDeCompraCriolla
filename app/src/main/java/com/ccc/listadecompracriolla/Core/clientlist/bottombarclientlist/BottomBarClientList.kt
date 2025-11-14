package com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist.coutesview.CoutesModal
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.oro
import com.ccc.listadecompracriolla.ui.theme.purpleHeart
import com.ccc.listadecompracriolla.ui.theme.white

@Composable
fun BottomClientList(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------Variables-----------------------------------------

    val total by viewModel.total.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val tasa by viewModel.tasa.collectAsState()
    var pressedPercent by remember { mutableStateOf(false) }


//------------------------------------------Variables-----------------------------------------
    val fontSizeTasa = 110.dp

    BottomAppBar(containerColor = purpleHeart) {


//------------------------------------------BottomInCarPrice-----------------------------------------
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Productos en carrito",
            modifier
                .size(40.dp)
                .padding(start = 5.dp),
            tint = white
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.widthIn(min = fontSizeTasa, max = fontSizeTasa)
        ) {
            Text(
                text = "En carrito",
                fontWeight = FontWeight.Bold,
                color = white
            )
            Text(
                text = formatNumber(inCar),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = white
            )

        }

        Box(contentAlignment = Alignment.Center) {
            VerticalDivider(
                color = white, thickness = 2.dp,
                modifier = modifier.padding(
                    vertical = 7.dp, horizontal = 20.dp
                )
            )
            Card(
                border = BorderStroke(1.dp, white), modifier = modifier.clickable(
                    onClick = { pressedPercent = true },
                ), colors = CardDefaults.cardColors(oro)
            ) {
                IconButton(onClick = { pressedPercent = true }) {
                    Icon(
                        painter = painterResource(R.drawable.percent_icon),
                        contentDescription = "Cuotas",
                    )
                }

            }

        }
//------------------------------------------BottomTotalPrice-----------------------------------------

        Icon(
            painter = if (tasa == 1f) painterResource(id = R.drawable.dinero) else painterResource(R.drawable.bs_icono),
            contentDescription = "Total",
            modifier = modifier
                .size(40.dp)
                .padding(start = 5.dp),
            tint = white
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .widthIn(min = fontSizeTasa, max = fontSizeTasa)
        ) {
            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold,
                color = white
            )
            Text(
                text = formatNumber(total),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = white
            )
        }
        if (pressedPercent) {
            CoutesModal(viewModel = viewModel, total = total) { pressedPercent = false }
        }
    }
}