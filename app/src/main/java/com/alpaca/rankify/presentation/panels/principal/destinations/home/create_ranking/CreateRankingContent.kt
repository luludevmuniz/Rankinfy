package com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.rankify.R
import com.alpaca.rankify.ui.theme.MEDIUM_PADDING
import com.alpaca.rankify.ui.theme.RankifyTheme

@Composable
fun CreateRankingContent(
    modifier: Modifier = Modifier,
    rankingName: String,
    rankingPassword: String,
    rankingNameError: Boolean,
    rankingPasswordError: Boolean,
    passwordVisible: Boolean,
    onRankingNameChange: (String) -> Unit,
    onRankingPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onCreateClick: () -> Unit,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = MEDIUM_PADDING)
    ) {
        Text(
            text = stringResource(R.string.novo_ranking),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        RankingNameOutlinedTextField(
            rankingName = rankingName,
            onRankingNameChange = { name ->
                onRankingNameChange(name)
            },
            rankingNameError = rankingNameError
        )
        PasswordOutlinedTextField(
            password = rankingPassword,
            onPasswordChange = { password ->
                onRankingPasswordChange(password)
            },
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = {
                onTogglePasswordVisibility()
            },
            isError = rankingPasswordError
        )
        CreateRankingButton(
            modifier = Modifier.align(Alignment.End),
            onCreateClick = {
                onCreateClick()
            },
            isLoading
        )
    }
}

@Composable
private fun RankingNameOutlinedTextField(
    rankingName: String,
    onRankingNameChange: (String) -> Unit,
    rankingNameError: Boolean
) {
    OutlinedTextField(
        value = rankingName,
        onValueChange = { name ->
            onRankingNameChange(name)
        },
        label = {
            Text(text = stringResource(R.string.nome_do_ranking))
        },
        singleLine = true,
        isError = rankingNameError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

@Composable
private fun PasswordOutlinedTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isError: Boolean
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = {
            Text(text = stringResource(R.string.senha_do_administrador))
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onTogglePasswordVisibility
            ) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    contentDescription = stringResource(R.string.change_password_visibility)
                )
            }
        },
        supportingText = {
            Text(text = stringResource(R.string.a_senha_deve_conter_no_minimo_6_digitos))
        },
        isError = isError
    )
}

@Composable
private fun CreateRankingButton(
    modifier: Modifier = Modifier,
    onCreateClick: () -> Unit,
    isLoading: Boolean
) {
    FilledTonalButton(
        modifier = modifier.animateContentSize(),
        onClick = {
            onCreateClick()
        },
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(text = "Criar ranking")
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateRankingContentPrev() {
    RankifyTheme {
        CreateRankingContent(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
            onCreateClick = { },
            rankingName = "",
            rankingPassword = "",
            rankingNameError = false,
            rankingPasswordError = false,
            passwordVisible = false,
            onRankingNameChange = {},
            onRankingPasswordChange = {},
            onTogglePasswordVisibility = {},
            isLoading = false
        )
    }
}