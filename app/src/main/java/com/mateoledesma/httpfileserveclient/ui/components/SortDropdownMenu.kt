package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SortDropdownMenu(
    modifier: Modifier = Modifier,
    isLinearLayout: Boolean,
    onChangeLayout: (Boolean) -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        IconButton(onClick = {
            isOpen = true
        }) {
            Icon(Icons.AutoMirrored.Rounded.Sort, contentDescription = "Sort")
        }
        DropdownMenu(
            modifier = Modifier
                .width(160.dp),
            expanded = isOpen,
            onDismissRequest = {
                isOpen = false
            }) {
            DropdownMenuRadioItem(
                text = {
                    Text(text = "List")
                },
                selected = isLinearLayout,
                onClick = {
                    onChangeLayout(true)
                }
            )

            DropdownMenuRadioItem(
                text = {
                    Text(text = "Grid")
                },
                selected = !isLinearLayout,
                onClick = {
                    onChangeLayout(false)
                }
            )
        }
    }

}