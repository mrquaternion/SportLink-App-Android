package com.mlr.sportlink.authentication.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun RoundedFilledButton(
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    fill: Color = MaterialTheme.colorScheme.primary,
    textColour: Color = MaterialTheme.colorScheme.onPrimary,
    isTappable: Boolean = true,
    action: () -> Unit,
    label: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = height),
        enabled = isTappable,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = fill,
            contentColor = textColour,
            disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        ),
        content = label,
    )
}

@Composable
fun RoundedStrokeButton(
    modifier: Modifier = Modifier,
    height: Dp = 40.dp,
    fill: Color = MaterialTheme.colorScheme.background,
    strokeColor: Color? = null,
    strokeWidth: Dp = 1.dp,
    isTappable: Boolean = true,
    action: () -> Unit,
    label: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = height),
        enabled = isTappable,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = strokeWidth,
            color = strokeColor ?: MaterialTheme.colorScheme.outline,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = fill,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = fill,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        ),
        content = label,
    )
}
