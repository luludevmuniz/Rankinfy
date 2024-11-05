package com.alpaca.rankify.presentation.panels.ranking_details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.presentation.panels.ranking_details.component.RankingItem
import com.alpaca.rankify.presentation.panels.ranking_details.component.SwipeableRankingItem
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.ui.theme.SMALL_PADDING
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingDetailsContent(
    modifier: Modifier = Modifier,
    isSyncing: () -> Boolean,
    isAdmin: () -> Boolean,
    lastUpdate: () -> String,
    players: () -> ImmutableList<Player>,
    onDeletePlayer: (Player) -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    val isRefreshing = remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        modifier = modifier,
        state = state,
        isRefreshing = isRefreshing.value,
        onRefresh = remember {
            {
                scope.launch {
                    isRefreshing.value = true
                    delay(1000)
                    isRefreshing.value = false
                    state.animateToHidden()
                }
            }
        },
    ) {
        RankingDetails(
            isSyncing = isSyncing,
            isAdmin = isAdmin,
            lastUpdate = lastUpdate,
            players = players,
            onDeletePlayer = onDeletePlayer,
            onEditPlayer = onEditPlayer
        )
    }
}

@Composable
@NonRestartableComposable
@OptIn(ExperimentalMaterial3Api::class)
private fun RankingDetails(
    isSyncing: () -> Boolean,
    isAdmin: () -> Boolean,
    lastUpdate: () -> String,
    players: () -> ImmutableList<Player>,
    onDeletePlayer: (Player) -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = MEDIUM_PADDING)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(EXTRA_LARGE_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSyncing()) {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                state = rememberTooltipState(),
                tooltip = {
                    PlainTooltip {
                        Text(text = stringResource(R.string.o_aplicativo_esta_sendo_sincronizado_com_o_servidor_as_informa_es_podem_estar_desatualizadas))
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Icons.Default.Warning,
                    tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
                    contentDescription = "Conteúdo não sincronizado"
                )
            }
        }
        Text(
            text = if (lastUpdate().isBlank())
                stringResource(R.string.o_ranking_ainda_nao_foi_salvo_no_servidor) else
                stringResource(
                    R.string.ultima_atualizacao,
                    lastUpdate()
                ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (players().isEmpty()) {
            EmptyPlayerListContent(isAdmin = isAdmin())
        } else {
            PlayersList(
                players = players,
                canEdit = isAdmin,
                onDeletePlayer = { onDeletePlayer(it) },
                onEditPlayer = { onEditPlayer(it) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EmptyPlayerListContent(isAdmin: Boolean) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
            state = rememberTooltipState(),
            tooltip = {
                if (isAdmin) {
                    RichTooltip(
                        title = {
                            Text(text = stringResource(R.string.adicione_jogadores))
                        }
                    ) {
                        Text(stringResource(R.string.clique_no_bot_o_flutuante_no_canto_inferior_da_tela_para_adicionar_jogadores_ao_ranking))
                    }
                } else {
                    PlainTooltip {
                        Text(text = stringResource(R.string.os_administradores_do_ranking_ainda_n_o_adicionaram_jogadores))
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = stringResource(R.string.ainda_nao_hajogadores_registrados_no_ranking),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PlayersList(
    modifier: Modifier = Modifier,
    canEdit: () -> Boolean,
    players: () -> ImmutableList<Player>,
    onDeletePlayer: (Player) -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        items(
            items = players(),
            key = { player -> player.id }
        ) { player ->
            if (canEdit()) {
                SwipeableRankingItem(
                    modifier = Modifier.animateItem(),
                    player = { player },
                    onEdit = onEditPlayer,
                    onDelete = onDeletePlayer
                )
            } else {
                RankingItem(
                    modifier = Modifier.animateItem(),
                    player = { player }
                )
            }
        }
    }
}