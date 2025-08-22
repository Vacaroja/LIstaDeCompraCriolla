package com.ccc.listadecompracriolla

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccc.listadecompracriolla.Core.ClientList
import com.ccc.listadecompracriolla.Core.CreateFood
import com.ccc.listadecompracriolla.Core.clientlist.ClientListScreen
import com.ccc.listadecompracriolla.Core.CreateFoodScreen
import com.ccc.listadecompracriolla.Core.Home
import com.ccc.listadecompracriolla.Core.HomeScreen
import com.ccc.listadecompracriolla.Core.clases.ProductViewModel


@Composable
fun NavegationControl(viewModel: ProductViewModel) {
    val navegationController = rememberNavController()
    NavHost(navController = navegationController, startDestination = ClientList) {
        composable<Home>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) }) {
            HomeScreen { navegationController.navigate(ClientList) }
        }
        composable<ClientList>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) }) {//pantalla de la lista de los productos
            ClientListScreen(viewModel = viewModel,
                navigateToCreateFood = { navegationController.navigate(CreateFood) }
            )
        }
        composable<CreateFood>(
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            popEnterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) },
            popExitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) }) {//pantalla para crear cada producto
            CreateFoodScreen(
                viewModel = viewModel,
                navigateToback = { navegationController.popBackStack() }
            )

        }

    }
}

