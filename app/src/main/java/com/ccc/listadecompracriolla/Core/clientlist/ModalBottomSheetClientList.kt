package com.ccc.listadecompracriolla.Core.clientlist


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ClientList
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetClientList(viewModel: ProductViewModel, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val clientL by viewModel.clientList.collectAsState()
    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState,

    ) {
        Button(onClick = {viewModel.addClientList(ClientList(id = clientL.size + 1,name = "coco"))}) { }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 40.dp)) {
            items(items = clientL){client ->
                Card(modifier = Modifier.heightIn(min = 20.dp).clickable{}) {
                    Text(text = "${client.name}")
                }
            }
        }
    }
}