package com.ccc.listadecompracriolla.Core.clientlist

import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.theme.Orange

@Composable
fun BottomClientList(modifier: Modifier = Modifier, viewModel: ProductViewModel) {
//------------------------------------------Variables-----------------------------------------

    val total by viewModel.total.collectAsState()
    val inCar by viewModel.inCar.collectAsState()
    val tasa by viewModel.tasa.collectAsState()
    val df = DecimalFormat("#.##")
//------------------------------------------Variables-----------------------------------------


    BottomAppBar(containerColor = Orange) {
//------------------------------------------BottomPresupuesto-----------------------------------------
//------------------------------------------animacion que revisar y ver si borrar-----------------------------------------


//------------------------------------------BottomInCarPrice-----------------------------------------
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Productos en carrito",
            modifier
                .size(40.dp)
                .padding(start = 5.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "En carrito",
                fontWeight = FontWeight.Bold
            )
            Text(text = "${df.format(inCar * tasa)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }

        Spacer(modifier.weight(1f))

        VerticalDivider(
            color = Color.Black, thickness = 2.dp,
            modifier = modifier.padding(
                vertical = 7.dp, horizontal = 5.dp
            )
        )
//------------------------------------------BottomTotalPrice-----------------------------------------
        Spacer(modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.dinero),
            contentDescription = "",
            modifier.size(40.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${df.format(total * tasa)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
}