package com.ccc.listadecompracriolla.Core.clientlist.top


import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.R

@Composable
fun DropDownMenuTop(
    expanded: Boolean,
    viewModel: ProductViewModel,
    onExpandex: () -> Unit,
    onDeleteAll:()-> Unit
) {
    val sortType by viewModel.sortType.collectAsState()
    var subMenuState by remember { mutableStateOf(false) }
    Box {
        DropdownMenu(//menu desplegable para medida
            expanded = expanded,
            onDismissRequest = { onExpandex() }
        ) {
            DropdownMenuItem(
                onClick = {
                    subMenuState = true
                    onExpandex()
                },
                text = { Text("Ordenar por") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.sort),
                        contentDescription = "Ordenar por"
                    )
                }
            )
            HorizontalDivider()
            DropdownMenuItem(
                onClick = {
                },
                text = { Text("Seleccionar todos") },
                leadingIcon = { Icon(painter =painterResource(R.drawable.check_all), contentDescription = "seleccionar todos") }

            )
            DropdownMenuItem(
                onClick = {

                },
                text = { Text("Deseleccionar todos") },
                leadingIcon = { Icon(painter =painterResource(R.drawable.uncheckall), contentDescription = "Deseleccionar todos") }


            )
            HorizontalDivider()
            DropdownMenuItem(
                onClick = {
                    onDeleteAll()
                },
                text = { Text("Borrar todos") },
                leadingIcon = { Icon(painter =painterResource(R.drawable.deleteall), contentDescription = "Borrar todos") }
            )
            HorizontalDivider()
            DropdownMenuItem(
                onClick = {
                },
                text = { Text("Envianos sugerencias") },
                leadingIcon = { Icon(painter =painterResource(R.drawable.addcomments), contentDescription = "Enviar comentarios") },
            )
            DropdownMenuItem(
                onClick = {
                },
                text = { Text("Calificanos <3") },
                leadingIcon = { Icon(painter =painterResource(R.drawable.starrate), contentDescription = "Enviar comentarios") },
            )

        }
        SubMenuTop(
            sortType = sortType,
            subMenuState = subMenuState,
            onExpandex = { onExpandex() },
            onDismiss = { subMenuState = false },
            onAlphabetical = { viewModel.changeSortTypeAlphabetical() }
        ) { viewModel.changeSortTypePrice() }
    }

}

@Composable
fun SubMenuTop(
    sortType: ProductViewModel.SortType,
    subMenuState: Boolean,
    onExpandex: () -> Unit,
    onDismiss: () -> Unit,
    onAlphabetical: () -> Unit,
    onPrice: () -> Unit,
) {
    DropdownMenu(//menu desplegable para medida
        expanded = subMenuState,
        onDismissRequest = { onDismiss() }
    ) {
        DropdownMenuItem(
            onClick = {
                onDismiss()
                onExpandex()
            },
            text = { Text("...") }
        )
        DropdownMenuItem(
            onClick = {
                onAlphabetical()
            },
            text = { Text(textSortMenu(sortType)) },
            leadingIcon = {
                Icon(
                    imageVector = iconSortMenuAlphabetical(sortType),
                    contentDescription = "ordenar alfabeticamente"
                )
            }

        )
        DropdownMenuItem(
            onClick = {
                onPrice()
            },
            text = { Text("Precio") },
            leadingIcon = {
                Icon(
                    imageVector = iconSortMenuPrice(sortType),
                    contentDescription = "ordenar alfabeticamente"
                )
            }
        )


    }
}

fun textSortMenu(sortType: ProductViewModel.SortType): String {
    return when (sortType) {
        ProductViewModel.SortType.NONE -> "Alfabetico"
        ProductViewModel.SortType.ALPHABETICAL -> "A-Z"
        ProductViewModel.SortType.REVERSE_ALPHABETICAL -> "Z-A"
        else -> "Alfabetico"
    }
}


fun iconSortMenuAlphabetical(sortType: ProductViewModel.SortType): ImageVector {
    return when (sortType) {
        ProductViewModel.SortType.NONE -> Icons.Default.ArrowDropDown
        ProductViewModel.SortType.ALPHABETICAL -> Icons.Default.KeyboardArrowDown
        ProductViewModel.SortType.REVERSE_ALPHABETICAL -> Icons.Default.KeyboardArrowUp
        else -> Icons.Default.ArrowDropDown

    }
}

fun iconSortMenuPrice(sortType: ProductViewModel.SortType): ImageVector {
    return when (sortType) {
        ProductViewModel.SortType.NONE -> Icons.Default.ArrowDropDown
        ProductViewModel.SortType.PRICE -> Icons.Default.KeyboardArrowDown
        ProductViewModel.SortType.REVERSE_PRICE -> Icons.Default.KeyboardArrowUp
        else -> Icons.Default.ArrowDropDown

    }
}
