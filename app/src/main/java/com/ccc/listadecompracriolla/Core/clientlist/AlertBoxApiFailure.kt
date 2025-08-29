package com.ccc.listadecompracriolla.Core.clientlist

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertBoxApiFailure(
    modifier: Modifier = Modifier, viewModel: ProductViewModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(onDismissRequest = {}, confirmButton = {}, dismissButton = {})

}