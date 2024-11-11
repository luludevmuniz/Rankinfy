package com.alpaca.rankify.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.alpaca.rankify.R
import com.alpaca.rankify.presentation.panels.principal.destinations.home.create_ranking.RankingPasswordUiState

@Composable
fun PasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    passwordState: () -> RankingPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = passwordState().value,
        onValueChange = onPasswordChange,
        label = {
            Text(text = stringResource(R.string.senha_do_administrador))
        },
        singleLine = true,
        visualTransformation = if (passwordState().isVisible) VisualTransformation.None
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
                    imageVector = if (passwordState().isVisible) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    contentDescription = stringResource(R.string.change_password_visibility)
                )
            }
        },
        supportingText = {
            Text(text = stringResource(R.string.a_senha_deve_conter_no_minimo_6_digitos))
        },
        isError = passwordState().error
    )
}