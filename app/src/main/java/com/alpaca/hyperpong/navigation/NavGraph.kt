package com.alpaca.hyperpong.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alpaca.hyperpong.navigation.Panel.RankingDetails
import com.alpaca.hyperpong.presentation.panels.principal.PrincipalScreen
import com.alpaca.hyperpong.presentation.panels.ranking_details.RankingScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Panel.Principal
    ) {
        composable<Panel.Principal> {
            PrincipalScreen(
                navigateToRanking = { id, adminPassword ->
                    navController.navigate(
                        RankingDetails(
                            id = id,
                            adminPassword = adminPassword
                        )
                    )
                }
            )
        }
        composable<RankingDetails> {
            RankingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}