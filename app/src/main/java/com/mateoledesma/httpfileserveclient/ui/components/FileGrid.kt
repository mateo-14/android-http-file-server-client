package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.utils.getThumbnailUrl

// TODO Add onAddToFavorite and onRemoveFromFavorite
@Composable
fun FileGrid(
    modifier: Modifier = Modifier,
    files: List<FileEntry>,
    onSelect: (FileEntry) -> Unit,
    onAddToFavorite: (FileEntry) -> Unit,
    onRemoveFromFavorite: (FileEntry) -> Unit,
    onClickFile: (FileEntry) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 140.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = files,
            key = { it.id }
        ) {
            FileGridItem(
                name = it.name,
                isDirectory = it.isDirectory,
                thumbnail = getThumbnailUrl(it),
                selected = it.isSelected,
                isFavorite = it.isFavorite,
                onClick = {
                    onClickFile(it)
                },
                onSelect = {
                    onSelect(it)
                },
                isSelectionMode = files.any { file -> file.isSelected },
                onAddToFavorite = {
                    onAddToFavorite(it)
                },
                onRemoveFromFavorite = {
                    onRemoveFromFavorite(it)
                }
            )
        }
    }
}