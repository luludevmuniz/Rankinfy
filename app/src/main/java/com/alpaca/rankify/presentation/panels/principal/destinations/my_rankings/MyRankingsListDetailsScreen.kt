package com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.alpaca.rankify.navigation.RankingDestination
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MyRankingsListDetail(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<RankingDestination>()
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                MyRankingsListScreen(
                    onRankingClicked = { ranking ->
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = ranking
                            )
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.contentKey?.let { ranking ->
                    RankingDetailsScreen(
                        rankingId = ranking.id,
                        onBackClick = {
                            scope.launch {
                                navigator.navigateBack()
                            }
                        }
                    )
                }
            }
        }
    )
}
