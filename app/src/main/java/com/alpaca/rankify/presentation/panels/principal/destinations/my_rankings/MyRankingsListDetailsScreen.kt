package com.alpaca.rankify.presentation.panels.principal.destinations.my_rankings

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.alpaca.rankify.navigation.RankingDestinationArgs
import com.alpaca.rankify.presentation.panels.ranking_details.RankingDetailsScreen
import com.alpaca.rankify.util.TestingTags.Ranking.MY_RANKINGS_PANEL
import com.alpaca.rankify.util.TestingTags.Ranking.RANKING_DETAILS_PANEL
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MyRankingsListDetail(
    modifier: Modifier = Modifier,
    rankingArgs: RankingDestinationArgs? = null,
    onBackClick: () -> Unit = {}
) {
    val inititalPane =
        if (rankingArgs == null) ListDetailPaneScaffoldRole.List else ListDetailPaneScaffoldRole.Detail
    val navigator = rememberListDetailPaneScaffoldNavigator<RankingDestinationArgs>(
        initialDestinationHistory = listOf(
            ThreePaneScaffoldDestinationItem(
                pane = inititalPane
            )
        )
    )
    val scope = rememberCoroutineScope()

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                MyRankingsListPanel(
                    modifier = Modifier.testTag(MY_RANKINGS_PANEL),
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
                RankingDetailsScreen(
                    modifier = modifier.testTag(RANKING_DETAILS_PANEL),
                    rankingArgs = rankingArgs ?: RankingDestinationArgs(
                        id = navigator.currentDestination?.contentKey?.id ?: -1,
                        adminPassword = navigator.currentDestination?.contentKey?.adminPassword
                    ),
                    onBackClick = {
                        scope.launch {
                            if (navigator.canNavigateBack()) {
                                navigator.navigateBack()
                            } else {
                                onBackClick()
                            }
                        }
                    }
                )
            }
        }
    )
}
