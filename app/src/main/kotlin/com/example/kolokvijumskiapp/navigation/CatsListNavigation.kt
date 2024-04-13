package com.example.kolokvijumskiapp.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kolokvijumskiapp.cats.details.catDetails
import com.example.kolokvijumskiapp.cats.list.cats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatsListNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cats",
    ){
        //TODO
        cats(
            route = "cats",
            onUserClick = {
                navController.navigate(route = "cats/$it")
            }
        )

        catDetails(
            route = "cats/{catId}",
            arguments = listOf(
                navArgument(name = "catId"){
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )

    }





}