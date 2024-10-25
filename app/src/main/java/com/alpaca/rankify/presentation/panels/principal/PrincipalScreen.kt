package com.alpaca.rankify.presentation.panels.principal

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.navigation.AppDestinations
import com.alpaca.rankify.presentation.panels.principal.destinations.home.HomeScreen
import com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings.MyRankingsScreen

@Composable
fun PrincipalScreen(
    navigateToRanking: (Long, String?) -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        Icon(
                            destination.icon,
                            contentDescription = stringResource(destination.contentDescription)
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.label)
                        )
                    },
                    selected = destination == currentDestination,
                    onClick = { currentDestination = destination }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.HOME -> {
                HomeScreen(
                    navigateToRanking = { id, adminPassword ->
                        navigateToRanking(id, adminPassword)
                    }
                )
            }

            AppDestinations.RANKINGS -> {
                MyRankingsScreen(
                    onRankingClicked = { id ->
                        navigateToRanking(id, null)
                    }
                )
            }
        }
    }
}