package com.mateoledesma.httpfileserveclient.ui.screens.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreadcrumbItem(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
    showArrow: Boolean,
    isSelected: Boolean = false,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        TextButton(
            modifier = modifier
                .defaultMinSize(
                    minHeight = 1.dp,
                    minWidth = 1.dp
                ),
            contentPadding = PaddingValues(
                start = 10.dp,
                end = if (showArrow) 5.dp else 10.dp,
                top = 6.dp,
                bottom = 6.dp
            ),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            onClick = onClick
        ) {
            Text(text)
            if (showArrow) {
                Icon(
                    Icons.Rounded.ChevronRight, contentDescription = "Next",
                    modifier = Modifier
                        .size(20.dp)
                        .padding(start = 4.dp)
                )

            }
        }
    }
}