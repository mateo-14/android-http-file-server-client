package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
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

@Composable
fun FilesOptionsDropdownMenu(
    modifier: Modifier = Modifier,
    onSelectAll: (() -> Unit)? = null,
    onAddToFavorites: (() -> Unit)? = null,
    onRemoveFromFavorites: (() -> Unit)? = null,
    onRandomFile: (() -> Unit)? = null,
) {
    var isOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { isOpen = true }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "More Options")
        }
        DropdownMenu(
            expanded = isOpen,
            onDismissRequest = {
                isOpen = false
            }) {
            if (onSelectAll != null) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Select all")
                    },
                    onClick = {
                        onSelectAll()
                        isOpen = false
                    }
                )
            }
            if (onAddToFavorites != null) {
                DropdownMenuItem(text = { Text("Add to favorite") }, onClick = {
                    onAddToFavorites()
                    isOpen = false
                })
            }
            if (onRemoveFromFavorites != null) {
                DropdownMenuItem(text = { Text("Remove from favorite") }, onClick = {
                    onRemoveFromFavorites()
                    isOpen = false
                })
            }
            if (onRandomFile != null) {
                DropdownMenuItem(text = { Text("Random file") }, onClick = {
                    onRandomFile()
                    isOpen = false
                })
            }
        }
    }
}