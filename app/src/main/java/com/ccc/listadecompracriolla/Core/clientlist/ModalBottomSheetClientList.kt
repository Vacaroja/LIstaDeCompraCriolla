package com.ccc.listadecompracriolla.Core.clientlist


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ClientList
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheetClientList(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onChange: (Int?) -> Unit,
    onAddNew: () -> Unit,
    onDelete: (Int?) -> Unit,
    onChangeName: (String?,Int?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val clientL by viewModel.clientList.collectAsState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,

        ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            ElevatedButton(onClick = {
                onAddNew()
            }) { Text("CREAR NUEVA LISTA") }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(items = clientL) { client ->
                    Card(
                        modifier = Modifier
                            .padding(6.dp).combinedClickable(onClick = {
                                onChange(client.id)
                                onDismiss()
                            }, onLongClick = {
                                onChangeName(client.name,client.id)
                            })

                    ) {
                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier.weight(1f))
                            Text(
                                text = "${client.name}",
                                modifier = modifier.padding(6.dp)
                            )

                            IconButton(onClick = { onDelete(client.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "borrar lista"
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}