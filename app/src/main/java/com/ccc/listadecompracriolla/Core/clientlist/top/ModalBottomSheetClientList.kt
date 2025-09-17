package com.ccc.listadecompracriolla.Core.clientlist.top


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel
import com.ccc.listadecompracriolla.Core.clientlist.AlertBoxDeleteClient

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ModalBottomSheetClientList(
    viewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onChange: (Int?) -> Unit,
    onAddNew: () -> Unit,
    onDelete: (Int?) -> Unit,
    onChangeName: (String?, Int?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val clientL by viewModel.clientList.collectAsState()
    var isDelete by remember { mutableStateOf(false) }
    var idToDelete by remember { mutableStateOf<Int?>(null) }
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier.weight(1f))
                ElevatedButton(onClick = { onAddNew() },modifier = modifier.padding(bottom = 20.dp)) {
                    Text("CREAR NUEVA LISTA")
                }
                Spacer(modifier.weight(1f))

            }
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(items = clientL) { client ->
                    Card(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(6.dp)
                            .combinedClickable(onClick = {
                                onChange(client.id)
                                onDismiss()
                            }, onLongClick = {
                                idToDelete = client.id
                                isDelete = true
                            })

                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            Text(
                                text = "${client.name}",
                                modifier = modifier
                                    .widthIn(min = 120.dp, max = 120.dp)
                                    .padding(6.dp)
                            )



                            IconButton(onClick = {
                                onChangeName(client.name, client.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "cambiar nombre de lista"
                                )
                            }


                        }
                    }
                }
            }
            if (isDelete) {
                AlertBoxDeleteClient(
                    onDismiss = {
                        isDelete = false
                        idToDelete = null
                    },
                    onConfirm = {
                        onDelete(idToDelete)
                        idToDelete = null
                        isDelete = false
                    },
                    anyText = "LA LISTA?",
                )
            }
        }
    }
}