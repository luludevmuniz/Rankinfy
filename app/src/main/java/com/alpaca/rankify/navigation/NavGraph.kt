package com.alpaca.rankify.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alpaca.rankify.navigation.Panel.Principal
import com.alpaca.rankify.navigation.Panel.RankingDetails
import com.alpaca.rankify.presentation.panel.principal.PrincipalScreen
import com.alpaca.rankify.presentation.panel.principal.destinations.my_rankings.MyRankingsListDetail

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Principal
    ) {
        composable<Principal>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
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
        composable<RankingDetails> { backStackEntry ->
            val rankingId = backStackEntry.arguments?.getLong("id")
            val adminPassword = backStackEntry.arguments?.getString("adminPassword")
            MyRankingsListDetail(
                rankingArgs = RankingDestinationArgs(
                    id = rankingId ?: -1,
                    adminPassword = adminPassword
                ),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}