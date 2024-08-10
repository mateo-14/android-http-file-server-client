package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
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
import com.mateoledesma.httpfileserveclient.data.SortBy

@Composable
fun SortDropdownMenu(
    modifier: Modifier = Modifier,
    isLinearLayout: Boolean,
    onChangeLayout: (Boolean) -> Unit,
    isSortAscending: Boolean,
    onChangeSortAscending: (Boolean) -> Unit,
    sortBy: SortBy,
    onChangeSortMode: (SortBy) -> Unit
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
                .width(180.dp),
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

            HorizontalDivider()

            DropdownMenuRadioItem(
                text = {
                    Text(text = "Name")
                },
                selected = sortBy == SortBy.NAME,
                onClick = {
                    onChangeSortMode(SortBy.NAME)
                }
            )

            DropdownMenuRadioItem(
                text = {
                    Text(text = "Type")
                },
                selected = sortBy == SortBy.TYPE,
                onClick = {
                    onChangeSortMode(SortBy.TYPE)
                }
            )

            DropdownMenuRadioItem(
                text = {
                    Text(text = "Size")
                },
                selected = sortBy == SortBy.SIZE,
                onClick = {
                    onChangeSortMode(SortBy.SIZE)
                }
            )

            DropdownMenuRadioItem(
                text = {
                    Text(text = "Last Modified")
                },
                selected = sortBy == SortBy.DATE,
                onClick = {
                    onChangeSortMode(SortBy.DATE)
                }
            )

            DropdownMenuCheckboxItem(
                checked = isSortAscending,
                text = { Text("Ascending") },
                onClick = { onChangeSortAscending(!isSortAscending) }
            )
        }
    }

}