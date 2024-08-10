package com.mateoledesma.httpfileserveclient.ui.screens.favorites.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.mateoledesma.httpfileserveclient.data.SortBy
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.components.FilesOptionsDropdownMenu
import com.mateoledesma.httpfileserveclient.ui.components.SearchTopBar
import com.mateoledesma.httpfileserveclient.ui.components.SelectedFilesTopBarIndicator
import com.mateoledesma.httpfileserveclient.ui.components.SortDropdownMenu
import com.mateoledesma.httpfileserveclient.ui.screens.home.components.expandAnimating

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    selectedFiles: List<FileEntry>,
    onClearSelectedFiles: () -> Unit,
    onSelectAll: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    onRandomFile: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    searchValue: String,
    isLinearLayout: Boolean,
    onChangeLayout: (Boolean) -> Unit,
    isSortAscending: Boolean,
    onChangeSortAscending: (Boolean) -> Unit,
    sortBy: SortBy,
    onChangeSortMode: (SortBy) -> Unit
) {
    val hasSelectedFiles = selectedFiles.isNotEmpty()
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = hasSelectedFiles) {
        if (selectedFiles.isNotEmpty()) {
            scrollBehavior?.expandAnimating()
        }
    }

    LaunchedEffect(key1 = isSearching, key2 = hasSelectedFiles) {
        if (isSearching && !hasSelectedFiles) {
            focusRequester.requestFocus()
        }

        if (!isSearching) {
            onSearchValueChange("")
        }
    }

    Column {
        TopAppBar(
            navigationIcon = {
                if (isSearching && !hasSelectedFiles) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { isSearching = false }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        SearchTopBar(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .focusRequester(focusRequester),
                            value = searchValue,
                            onValueChange = onSearchValueChange,
                            placeholder = "Search"
                        )
                    }
                }

                if (hasSelectedFiles) {
                    SelectedFilesTopBarIndicator(
                        count = selectedFiles.size,
                        onClearSelectedFiles = { onClearSelectedFiles() }
                    )
                }
            },
            title = {
                if (!hasSelectedFiles && !isSearching) {
                    Text("Favorites")
                }
            },
            scrollBehavior = scrollBehavior,
            actions = {
                if (!hasSelectedFiles && !isSearching) {
                    IconButton(onClick = { isSearching = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    SortDropdownMenu(
                        isLinearLayout = isLinearLayout,
                        onChangeLayout = onChangeLayout,
                        isSortAscending = isSortAscending,
                        onChangeSortAscending = onChangeSortAscending,
                        sortBy = sortBy,
                        onChangeSortMode = onChangeSortMode
                    )
                }

                FilesOptionsDropdownMenu(
                    onSelectAll = onSelectAll,
                    onRemoveFromFavorites = if (hasSelectedFiles) onRemoveFromFavorites else null,
                    onRandomFile = onRandomFile
                )
            }
        )

    }
}

