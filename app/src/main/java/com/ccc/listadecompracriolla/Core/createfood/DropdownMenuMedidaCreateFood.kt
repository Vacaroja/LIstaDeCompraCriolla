package com.ccc.listadecompracriolla.Core.createfood

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCreateFood(
    modifier: Modifier = Modifier,
    unidad: String,
    expanded: Boolean,
    medidaList: List<String>,
    onExpandex: () -> Unit,
    onChangeUnidad: (String) -> Unit,

    ) {
    ExposedDropdownMenuBox(//menu desplegable para medida
        expanded = expanded,
        onExpandedChange = { onExpandex }
    ) {
        OutlinedTextField(

            value = unidad,
            onValueChange = { },
            label = { Text("Medida") },
            readOnly = true,
            modifier = modifier
                .width(100.dp)
                .heightIn(min = 56.dp)
                .menuAnchor(MenuAnchorType.PrimaryEditable, true),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = MaterialTheme.shapes.medium
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandex() }) {
            medidaList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onChangeUnidad(item)
                        onExpandex()
                    }
                )
            }
        }
    }
}