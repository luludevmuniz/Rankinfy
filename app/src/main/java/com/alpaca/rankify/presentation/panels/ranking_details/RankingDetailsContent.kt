package com.alpaca.rankify.presentation.panels.ranking_details

import android.content.res.Configuration
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
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alpaca.rankify.R
import com.alpaca.rankify.domain.model.Player
import com.alpaca.rankify.domain.model.Ranking
import com.alpaca.rankify.presentation.common.SwipeBox
import com.alpaca.rankify.presentation.panels.ranking_details.component.RankingItem
import com.alpaca.rankify.ui.theme.EXTRA_LARGE_PADDING
import com.alpaca.rankify.ui.theme.RankifyTheme
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.ui.theme.SMALL_PADDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingContent(
    modifier: Modifier = Modifier,
    uiState: RankingDetailsUiState,
    ranking: Ranking,
    onDeletePlayer: (Player) -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = MEDIUM_PADDING)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(EXTRA_LARGE_PADDING)
    ) {
        if (uiState.isSyncing) {
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
        if (ranking.formattedLastUpdated.isBlank()) {
            Text(
                text = stringResource(R.string.o_ranking_ainda_nao_foi_salvo_no_servidor),
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Text(
                text = stringResource(
                    R.string.ultima_atualizacao,
                    ranking.formattedLastUpdated
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (ranking.players.isEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
                    state = rememberTooltipState(),
                    tooltip = {
                        if (ranking.isAdmin) {
                            RichTooltip(
                                title = {
                                    Text(text = "Adicione jogadores")
                                }
                            ) {
                                Text("Clique no botão flutuante no canto inferior da tela para adicionar jogadores ao ranking.")
                            }
                        } else {
                            PlainTooltip {
                                Text(text = "Os administradores do ranking ainda não adicionaram jogadores.")
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
        } else {
            PlayersList(
                players = ranking.sortedPlayers,
                canEdit = ranking.isAdmin,
                onDeletePlayer = onDeletePlayer,
                onEditPlayer = onEditPlayer
            )
        }
    }
}

@Composable
private fun PlayersList(
    modifier: Modifier = Modifier,
    players: List<Player>,
    canEdit: Boolean,
    onDeletePlayer: (Player) -> Unit,
    onEditPlayer: (Player) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
    ) {
        items(items = players) { player ->
            if (canEdit) {
                SwipeBox(
                    modifier = Modifier.animateItem(),
                    onDelete = {
                        onDeletePlayer(player)
                    },
                    onEdit = {
                        onEditPlayer(player)
                    }
                ) {
                    RankingItem(player = player)
                }
            } else {
                RankingItem(
                    modifier = Modifier.animateItem(),
                    player = player
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RankingContentPrev() {
    RankifyTheme {
    }
}