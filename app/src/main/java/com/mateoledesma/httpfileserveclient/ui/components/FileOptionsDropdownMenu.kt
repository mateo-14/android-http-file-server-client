package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FileOptionsDropdownMenu(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onAddToFavorite: () -> Unit,
    onRemoveFromFavorite: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = {
            isMenuExpanded = true
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
        }
        DropdownMenu(
            modifier = Modifier.defaultMinSize(
                minWidth = 180.dp
            ),
            expanded = isMenuExpanded, onDismissRequest = {
                isMenuExpanded = false
            }) {
            DropdownMenuItem(text = {
                Text(
                    text = if (isFavorite) "Remove from favorites" else "Add to favorites"
                )
            }, onClick = {
                isMenuExpanded = false
                if (isFavorite) {
                    onRemoveFromFavorite()
                } else {
                    onAddToFavorite()
                }
            })
        }
    }
}