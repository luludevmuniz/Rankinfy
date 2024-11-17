package com.alpaca.rankify.presentation.panel.principal

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.navigation.AppDestinations
import com.alpaca.rankify.presentation.panel.principal.destinations.home.HomePanel
import com.alpaca.rankify.presentation.panel.principal.destinations.my_rankings.MyRankingsListDetail

@Composable
fun PrincipalScreen(
    modifier: Modifier = Modifier,
    navigateToRanking: (Long, String?) -> Unit,
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    modifier = Modifier.testTag(destination.testTag),
                    icon = {
                        Icon(
                            painter = painterResource(destination.icon),
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
                HomePanel(
                    navigateToRanking = { id, adminPassword ->
                        navigateToRanking(id, adminPassword)
                    }
                )
            }

            AppDestinations.RANKINGS -> {
                MyRankingsListDetail()
            }
        }
    }
}