package com.alpaca.rankify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alpaca.rankify.navigation.Panel.Principal
import com.alpaca.rankify.navigation.Panel.RankingDetails
import com.alpaca.rankify.presentation.panels.principal.PrincipalScreen
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Principal
    ) {
        composable<Principal> {
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
            RankingDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}