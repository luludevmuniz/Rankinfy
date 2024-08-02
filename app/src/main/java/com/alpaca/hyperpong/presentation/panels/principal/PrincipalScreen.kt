package com.alpaca.hyperpong.presentation.panels.principal

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.alpaca.hyperpong.navigation.AppDestinations
import com.alpaca.hyperpong.presentation.panels.principal.destinations.home.HomeScreen
import com.alpaca.hyperpong.presentation.panels.principal.destinations.my_rankings.MyRankingsScreen

@Composable
fun PrincipalScreen(
    navigateToRanking: (Long, String?) -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = stringResource(it.contentDescription)
                        )
                    },
                    label = { Text(stringResource(it.label)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
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