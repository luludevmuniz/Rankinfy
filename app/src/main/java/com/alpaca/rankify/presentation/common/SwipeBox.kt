package com.alpaca.rankify.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.alpaca.rankify.R

@Composable
fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()

    var icon: Painter? = null
    var alignment: Alignment? = null
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            icon = painterResource(R.drawable.ic_delete)
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.errorContainer
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            icon = painterResource(R.drawable.ic_edit)
            alignment = Alignment.CenterStart
            color = Color.Green.copy(alpha = 0.3f)
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = null
            alignment = Alignment.CenterEnd
            color = Color.Transparent
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CardDefaults.shape)
                    .background(color)
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier.minimumInteractiveComponentSize(),
                        painter = it,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(Unit) {
                onDelete()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(Unit) {
                onEdit()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {}
    }
}