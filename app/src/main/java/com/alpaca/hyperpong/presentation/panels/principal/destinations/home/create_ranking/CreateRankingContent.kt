package com.alpaca.hyperpong.presentation.panels.principal.destinations.home.create_ranking

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.alpaca.hyperpong.R
import com.alpaca.hyperpong.ui.theme.HyperPongTheme
import com.alpaca.hyperpong.ui.theme.MEDIUM_PADDING

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
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = MEDIUM_PADDING,
            alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = stringResource(R.string.criar_ranking),
            style = MaterialTheme.typography.titleLarge
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
            onCreateClick = {
                onCreateClick()
            },
            isLoading
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun RankingNameOutlinedTextField(
    rankingName: String,
    onRankingNameChange: (String) -> Unit,
    rankingNameError: Boolean
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        state = rememberTooltipState(),
        tooltip = {
            PlainTooltip {
                Text(text = stringResource(R.string.nome_que_os_participantes_deverao_pesquisar_para_encontrar_ranking))
            }
        }
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
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PasswordOutlinedTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    isError: Boolean
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        state = rememberTooltipState(),
        tooltip = {
            PlainTooltip {
                Text(text = stringResource(R.string.somente_quem_tem_a_senha_do_administrador_pode_editar_o_ranking))
            }
        }
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
                        contentDescription = null
                    )
                }
            },
            supportingText = {
                Text(text = stringResource(R.string.a_senha_deve_conter_no_minimo_6_digitos))
            },
            isError = isError
        )
    }
}

@Composable
private fun CreateRankingButton(
    onCreateClick: () -> Unit,
    isLoading: Boolean
) {
    FilledTonalButton(
        modifier = Modifier.animateContentSize(),
        onClick = {
            onCreateClick()
        }
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
    HyperPongTheme {
        CreateRankingContent(
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