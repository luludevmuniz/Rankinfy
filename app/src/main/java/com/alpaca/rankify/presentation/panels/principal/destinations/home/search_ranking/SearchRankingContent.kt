package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.alpaca.rankify.R
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING

@Composable
fun SearchRankingContent(
    modifier: Modifier = Modifier,
    searchRankingUiState: () -> SearchRankingUiState,
    onRankingIdChange: (String) -> Unit = {},
    onSearchRankingClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            MEDIUM_PADDING,
            Alignment.CenterVertically
        )
    ) {
        OutlinedTextField(
            value = searchRankingUiState().rankingId,
            onValueChange = onRankingIdChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            label = {
                Text(stringResource(R.string.id_do_ranking))
            },
            placeholder = {
                Text(stringResource(R.string.apenas_numeros))
            },
            singleLine = true,
            prefix = {
                Text("#")
            },
            isError = searchRankingUiState().rankingIdError
        )
        FilledTonalButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onSearchRankingClick
        ) {
            Text(stringResource(R.string.buscar_ranking))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun SearchRankingContentPrev() {
//    RankifyTheme {
//        SearchRankingContent(
//            rankingIdError = false,
//            rankingId = "123",
//            onRankingIdChange = { },
//            onSearchRankingClick = { }
//        )
//    }
//}