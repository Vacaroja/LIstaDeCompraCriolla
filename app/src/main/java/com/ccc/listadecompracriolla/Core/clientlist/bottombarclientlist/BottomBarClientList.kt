package com.ccc.listadecompracriolla.Core.clientlist.bottombarclientlist

import android.icu.text.DecimalFormat
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.formatterdata.formatNumber
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange
import com.ccc.listadecompracriolla.ui.theme.black

@Composable
fun BottomClientList(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------Variables-----------------------------------------

    val total by viewModel.total.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val df = DecimalFormat("#.##")

//------------------------------------------Variables-----------------------------------------
    val fontSizeTasa = 120.dp

    BottomAppBar(containerColor = Orange) {


//------------------------------------------BottomInCarPrice-----------------------------------------
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Productos en carrito",
            modifier
                .size(40.dp)
                .padding(start = 5.dp),
            tint = black
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.widthIn(min = fontSizeTasa, max = fontSizeTasa)
        ) {
            Text(
                text = "En carrito",
                fontWeight = FontWeight.Bold,
                color = black
            )
            Text(
                text = formatNumber(df.format(inCar).toFloat()),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = black
            )

        }



        VerticalDivider(
            color = Color.Black, thickness = 2.dp,
            modifier = modifier.padding(
                vertical = 7.dp, horizontal = 20.dp
            )
        )
//------------------------------------------BottomTotalPrice-----------------------------------------

        Icon(
            painter = painterResource(id = R.drawable.dinero),
            contentDescription = "",
            modifier
                .size(40.dp)
                .padding(start = 5.dp),
            tint = black
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .widthIn(min = fontSizeTasa, max = fontSizeTasa)
        ) {
            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold,
                color = black
            )
            Text(
                text = formatNumber(df.format(total).toFloat()),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = black
            )
        }
    }
}