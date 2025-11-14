package com.ccc.listadecompracriolla.Core.clientlist.principal.ratesbuttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R
import com.ccc.listadecompracriolla.ui.buttoncolor.animateColorButtonClientList
import com.ccc.listadecompracriolla.ui.theme.black
import com.ccc.listadecompracriolla.ui.theme.greenApple

@Composable
fun RateButton(
    viewModel: ProductViewModel,
    isPressed: Int,
    modifier: Modifier = Modifier,
    onFailureApi: () -> Unit,
    showActualRate: () -> Unit
) {
    /*
    1 -> Dolares
    2 -> bolivares * Dollar
    3 -> bolivares * Euro
     */

    val animatedColorDollar by animateColorButtonClientList(isPressed)
    val animatedColorBs by animateColorButtonClientList(isPressed,2)
    val animatedColorEur by animateColorButtonClientList(isPressed,3)
    val animatedColorNoPressedDollar by animateColorButtonClientList(isPressed,primaryColor = greenApple, secondaryColor = black)
    val animatedColorNoPressedBs by animateColorButtonClientList(isPressed,2,primaryColor = greenApple, secondaryColor = black)
    val animatedColorNoPressedEur by animateColorButtonClientList(isPressed,3,primaryColor = greenApple, secondaryColor = black)



    Card {
        LazyRow(modifier.padding(5.dp)) {


//------------------------------------------DolarToDolar-----------------------------------------
            item {
                Card(
                    modifier
                        .clickable {
                            viewModel.actualizarTasa(ProductViewModel.TipoConversion.DIRECTA)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = animatedColorDollar
                    )
                ) {//BOTON PARA LAS CAMBIAR A DOLAR
                    Icon(
                        painter = painterResource(id = R.drawable.dolar),
                        contentDescription = "Settings",
                        modifier = modifier
                            .size(50.dp)
                            .padding(5.dp),
                        tint = animatedColorNoPressedDollar
                    )
                }
            }
//------------------------------------------DolarToBCV-----------------------------------------
            item {
                Card(
                    modifier.padding(horizontal = 5.dp).clickable {

                        if (viewModel.validTasa()) {
                            onFailureApi()
                        } else {
                            showActualRate()
                            viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BCV)
                        }
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = animatedColorBs
                    )
                ) {//BOTON PARA LAS CAMBIAR A DOLAR BCV
                    Icon(
                        painter = painterResource(id = R.drawable.bsdollar),
                        contentDescription = "DollarBCV",
                        modifier = modifier
                            .size(50.dp)
                            .padding(5.dp),
                        tint = animatedColorNoPressedBs
                    )
                }
            }
            item {
                Card(
                    modifier.clickable {

                        if (viewModel.validTasa()) {
                            onFailureApi()
                        } else {
                            showActualRate()
                            viewModel.actualizarTasa(ProductViewModel.TipoConversion.DOLAR_A_BS_EUR)
                        }
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = animatedColorEur
                    )
                ) {//BOTON PARA LAS CAMBIAR A DOLAR BCV
                    Icon(
                        painter = painterResource(id = R.drawable.bseur),
                        contentDescription = "EuroBCV",
                        modifier = modifier
                            .size(50.dp)
                            .padding(5.dp),
                        tint = animatedColorNoPressedEur
                    )
                }
            }

        }
    }
}