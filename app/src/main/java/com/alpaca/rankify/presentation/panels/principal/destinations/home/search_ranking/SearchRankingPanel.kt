package com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.common.PasswordOutlinedTextField
import com.alpaca.rankify.presentation.common.RankingIdOutlinedTextField
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingNameUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingPasswordUiState
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ChangeIsAdministrator
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.HideLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.RequestIdle
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.SearchRanking
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ShowLoading
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.ToggleAdminPasswordVisibility
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateRankingAdminPassword
import com.alpaca.rankify.presentation.panels.principal.destinations.home.search_ranking.SearchRankingEvent.UpdateSearchedId
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.util.RequestState

@Composable
fun SearchRankingPanel(
    modifier: Modifier = Modifier,
    searchRankingViewModel: SearchRankingViewModel = hiltViewModel(),
    navigateToRanking: (Long, String?) -> Unit,
    showSnackBar: (String) -> Unit
) {
    val searchRankingUiState by searchRankingViewModel.uiState.collectAsStateWithLifecycle()
    val rankingRequestState by searchRankingViewModel.rankingRequestState.collectAsStateWithLifecycle()
    val rankingIdUiState by searchRankingViewModel.rankingIdUiState.collectAsStateWithLifecycle()
    val rankingAdminPasswordUiState by searchRankingViewModel.rankingAdminPasswordUiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(rankingRequestState) {
        when (rankingRequestState) {
            is RequestState.Success -> {
                searchRankingViewModel.onEvent(HideLoading)
                navigateToRanking(
                    rankingRequestState.getSuccessData(),
                    rankingAdminPasswordUiState.value
                )
                searchRankingViewModel.onEvent(RequestIdle)
            }

            is RequestState.Error -> {
                searchRankingViewModel.onEvent(HideLoading)
                showSnackBar(rankingRequestState.getErrorMessage())
                searchRankingViewModel.onEvent(RequestIdle)
            }

            is RequestState.Loading -> {
                searchRankingViewModel.onEvent(ShowLoading)
                keyboardController?.hide()
            }

            else -> {}
        }
    }
    SearchRankingContent(
        modifier = modifier,
        searchRankingUiState = { searchRankingUiState },
        idUiState = { rankingIdUiState },
        passwordState = { rankingAdminPasswordUiState },
        onRankingIdChange = remember {
            { id ->
                searchRankingViewModel.onEvent(UpdateSearchedId(id = id))
            }
        },
        onSearchRankingClick = remember {
            {
                searchRankingViewModel.onEvent(SearchRanking)
            }
        },
        onRankingPasswordChange = remember {
            { password ->
                searchRankingViewModel.onEvent(
                    UpdateRankingAdminPassword(password = password)
                )
            }
        },
        onTogglePasswordVisibility = remember {
            {
                searchRankingViewModel.onEvent(ToggleAdminPasswordVisibility)
            }
        },
        onIsAdministratorChange = remember {
            {
                searchRankingViewModel.onEvent(ChangeIsAdministrator)
            }
        }
    )
}

@NonRestartableComposable
@Composable
fun SearchRankingContent(
    modifier: Modifier = Modifier,
    searchRankingUiState: () -> SearchRankingUiState,
    onIsAdministratorChange: () -> Unit,
    idUiState: () -> RankingNameUiState,
    onRankingIdChange: (String) -> Unit,
    onSearchRankingClick: () -> Unit,
    passwordState: () -> RankingPasswordUiState,
    onRankingPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        RankingIdTextField(
            idUiState = idUiState,
            onRankingIdChange = onRankingIdChange,
            searchRankingUiState = searchRankingUiState
        )
        AdminPasswordTextField(
            searchRankingUiState = searchRankingUiState,
            passwordState = passwordState,
            onRankingPasswordChange = onRankingPasswordChange,
            onTogglePasswordVisibility = onTogglePasswordVisibility
        )
        AdminCheckBox(
            searchRankingUiState = searchRankingUiState,
            onIsAdministratorChange = onIsAdministratorChange,
        )
        SearchRankingButton(
            onSearchRankingClick = onSearchRankingClick,
            searchRankingUiState = searchRankingUiState
        )
    }
}

@Composable
private fun RankingIdTextField(
    idUiState: () -> RankingNameUiState,
    onRankingIdChange: (String) -> Unit,
    searchRankingUiState: () -> SearchRankingUiState
) {
    RankingIdOutlinedTextField(
        nameState = idUiState,
        onRankingIdChange = { id ->
            onRankingIdChange(id)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (searchRankingUiState().isAdministrator)
                ImeAction.Next else
                ImeAction.Done
        ),
        label = {
            Text(stringResource(R.string.id_do_ranking))
        },
        placeholder = {
            Text(stringResource(R.string.apenas_numeros))
        },
        prefix = {
            Text("#")
        }
    )
}

@Composable
private fun ColumnScope.AdminPasswordTextField(
    searchRankingUiState: () -> SearchRankingUiState,
    passwordState: () -> RankingPasswordUiState,
    onRankingPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit
) {
    Spacer(modifier = Modifier.height(MEDIUM_PADDING))
    AnimatedVisibility(visible = searchRankingUiState().isAdministrator) {
        PasswordOutlinedTextField(
            passwordState = passwordState,
            onPasswordChange = { password ->
                onRankingPasswordChange(password)
            },
            onTogglePasswordVisibility = {
                onTogglePasswordVisibility()
            },
        )
    }
}

@Composable
private fun AdminCheckBox(
    searchRankingUiState: () -> SearchRankingUiState,
    onIsAdministratorChange: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable { onIsAdministratorChange() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = searchRankingUiState().isAdministrator,
            onCheckedChange = {
                onIsAdministratorChange()
            }
        )
        Text(
            text = stringResource(R.string.sou_administrador_do_ranking),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ColumnScope.SearchRankingButton(
    onSearchRankingClick: () -> Unit,
    searchRankingUiState: () -> SearchRankingUiState
) {
    FilledTonalButton(
        modifier = Modifier.Companion.align(Alignment.End),
        onClick = onSearchRankingClick,
        enabled = searchRankingUiState().isLoading.not()
    ) {
        if (searchRankingUiState().isLoading) {
            CircularProgressIndicator()
        } else {
            Text(stringResource(R.string.buscar_ranking))
        }
    }
}