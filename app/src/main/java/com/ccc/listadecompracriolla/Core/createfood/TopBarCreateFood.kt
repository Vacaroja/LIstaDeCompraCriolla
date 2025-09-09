package com.ccc.listadecompracriolla.Core.createfood

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.ccc.listadecompracriolla.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMenuCreateFood(
    enableButton: Boolean,
    navigateToback: () -> Unit,
    saveProduct: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    TopAppBar(
        title = {
            Text(text = "CREACION DE PRODUCTO", fontWeight = FontWeight.Bold)

        },
        navigationIcon = {

            IconButton(onClick = { navigateToback() }, enabled = enableButton) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

        },
        actions = {
            IconButton(onClick = { saveProduct() }, enabled = enableButton) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Listo"
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarColors(
            containerColor = Orange,
            scrolledContainerColor = Orange,
            navigationIconContentColor = Color.Black,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Unspecified

        )
    )

}