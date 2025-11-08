package com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.white
import com.ccc.listadecompracriolla.ui.theme.purpleHeart

@Composable
fun BottomClientList(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------Variables-----------------------------------------

    val total by viewModel.total.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val tasa by viewModel.tasa.collectAsState()


//------------------------------------------Variables-----------------------------------------
    val fontSizeTasa = 120.dp

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

        VerticalDivider(
            color = white, thickness = 2.dp,
            modifier = modifier.padding(
                vertical = 7.dp, horizontal = 20.dp
            )
        )
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
    }
}