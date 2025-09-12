package com.ccc.listadecompracriolla.Core.clientlist.top


import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

@Composable
fun DropDownMenuTop(
    expanded: Boolean,
    viewModel: ProductViewModel,
    onExpandex: () -> Unit,
) {

    Box(){
        DropdownMenu(//menu desplegable para medida
            expanded = expanded,
            onDismissRequest = { onExpandex() }
        ) {
            DropdownMenuItem(
                onClick = {
                    viewModel.changeSortType()
                    onExpandex()
                },
                text = { Text("Normal") }
            )
            DropdownMenuItem(
                onClick = {
                    viewModel.changeSortType(ProductViewModel.SortType.ALPHABETICAL)
                    onExpandex()
                },
                text = { Text("A-Z") }
            )
            DropdownMenuItem(
                onClick = {
                    viewModel.changeSortType(ProductViewModel.SortType.REVERSE_ALPHABETICAL)
                    onExpandex()
                },
                text = { Text("Z-A") }
            )

        }
    }

}