package com.mateoledesma.httpfileserveclient.ui.screens.favorites.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.components.FilesOptionsDropdownMenu
import com.mateoledesma.httpfileserveclient.ui.components.SelectedFilesTopBarIndicator
import com.mateoledesma.httpfileserveclient.ui.components.SortDropdownMenu
import com.mateoledesma.httpfileserveclient.ui.screens.home.components.expandAnimating

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isLinearLayout: Boolean,
    selectedFiles: List<FileEntry>,
    onClearSelectedFiles: () -> Unit,
    onLayoutChange: (Boolean) -> Unit,
    onSelectAll: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    onRandomFile: () -> Unit,
) {
    val hasSelectedFiles = selectedFiles.isNotEmpty()

    LaunchedEffect(key1 = hasSelectedFiles) {
        if (selectedFiles.isNotEmpty()) {
            scrollBehavior?.expandAnimating()
        }
    }

    Column {
        TopAppBar(
            title = {
                if (hasSelectedFiles) {
                    SelectedFilesTopBarIndicator(
                        count = selectedFiles.size,
                        onClearSelectedFiles = { onClearSelectedFiles() }
                    )
                } else {
                    Text("Favorites")
                }
            },
            scrollBehavior = scrollBehavior,
            actions = {
                if (!hasSelectedFiles) {
                    SortDropdownMenu(
                        isLinearLayout = isLinearLayout,
                        onChangeLayout = onLayoutChange
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
