package com.alpaca.rankify.presentation.panels.principal.destinations.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.alpaca.rankify.navigation.TabsDestinations
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.CreateRankingPanel
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingPanel
import com.alpaca.rankify.util.TestingTags.Ranking.CREATE_RANKING_PANEL
import com.alpaca.rankify.util.TestingTags.Ranking.SEARCH_RANKING_PANEL

@Composable
@NonRestartableComposable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeContent(
    modifier: Modifier = Modifier,
    navigateToRanking: (Long, String?) -> Unit,
    showSnackBar: (String) -> Unit
) {
    val tabs = listOf(TabsDestinations.CREATE_RANKING, TabsDestinations.SEARCH_RANKING)
    val tabIndex = remember { mutableIntStateOf(0) }
    val focusManager = LocalFocusManager.current
    SideEffect {
        focusManager.clearFocus()
    }
    Column(
        modifier = modifier
    ) {
        SecondaryTabRow(
            selectedTabIndex = tabIndex.intValue,
            tabs = {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        modifier = Modifier.testTag(tab.testTag),
                        text = {
                            Text(stringResource(tab.label))
                        },
                        selected = tabIndex.intValue == index,
                        onClick = {
                            tabIndex.intValue = index
                        }
                    )
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (tabs[tabIndex.intValue]) {
                TabsDestinations.CREATE_RANKING -> CreateRankingPanel(
                    modifier = modifier.testTag(CREATE_RANKING_PANEL),
                    navigateToRanking = navigateToRanking,
                    showSnackBar = showSnackBar
                )
                TabsDestinations.SEARCH_RANKING -> SearchRankingPanel(
                    modifier = modifier.testTag(SEARCH_RANKING_PANEL),
                    navigateToRanking = navigateToRanking,
                    showSnackBar = showSnackBar
                )
            }
        }
    }
}