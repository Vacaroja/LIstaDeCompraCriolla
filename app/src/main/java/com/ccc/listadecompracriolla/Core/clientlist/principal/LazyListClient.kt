package com.ccc.listadecompracriolla.Core.clientlist.principal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel

@Composable
fun LazyListClient(
    modifier: Modifier = Modifier,
    innerpadding: PaddingValues,
    viewModel: ProductViewModel,
    scrollState: LazyListState,
    onNavigateToCreateFood: () -> Unit,

    ) {

    val actualProducts by viewModel.actualDobleProductList.collectAsState()
    val dividerCheckedItems by viewModel.dividerCheckedItems.collectAsState()
    val selected by viewModel.selectedProductIds.collectAsState()
    val onSelected:(Int)->Unit = {idProd ->
        viewModel.toggleProductSelection(idProd)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(innerpadding),
        state = scrollState,
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        //reciclerview to watch items or products
        items(
            items = actualProducts.first,
            key = { it.id!! }) { producto ->
                ProducIterator(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 350), // Animation for when item appears
                        fadeOutSpec = tween(durationMillis = 250), // Animation for when item disappears
                        placementSpec = spring( // Animation for when item reorders
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )),
                    product = producto,
                    viewModel = viewModel,
                    selected = producto.id in selected,
                    onChangeProduct = { idProduct ->
                        viewModel.actualizeProduct(idProduct)
                        onNavigateToCreateFood()
                    },
                    onSelected = {id ->
                        onSelected(id!!)
                    },
                )

        }

            item {
                AnimatedVisibility (dividerCheckedItems, enter = fadeIn(), exit = fadeOut()){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(
                            modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                        Text(
                            "Listos",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                        HorizontalDivider(
                            modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                }
            }

        items(
            items = actualProducts.second,
            key = { it.id!! }) { producto ->
                ProducIterator(
                    modifier = Modifier.animateItem(
                        fadeInSpec = tween(durationMillis = 350), // Animation for when item appears
                        fadeOutSpec = tween(durationMillis = 250), // Animation for when item disappears
                        placementSpec = spring( // Animation for when item reorders
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )),
                    product = producto,
                    viewModel = viewModel,
                    selected = producto.id in selected,
                    onChangeProduct = { idProduct ->
                        viewModel.actualizeProduct(idProduct)
                        onNavigateToCreateFood()
                    },
                    onSelected = { id ->
                        onSelected(id!!)}
                )


        }
    }
}