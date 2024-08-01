package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.utils.getThumbnailUrl

@Composable
fun FileList(
    modifier: Modifier = Modifier,
    files: List<FileEntry>,
    onSelect: (FileEntry) -> Unit,
    onAddToFavorite: (FileEntry) -> Unit,
    onRemoveFromFavorite: (FileEntry) -> Unit,
    onClickFile: (FileEntry) -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = files,
            key = { it.id }
        ) {
            FileListItem(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .height(80.dp)
                    .fillMaxWidth(),
                name = it.name,
                isDirectory = it.isDirectory,
                selected = it.isSelected,
                isFavorite = it.isFavorite,
                thumbnail = getThumbnailUrl(it),
                onClick = {
                    onClickFile(it)
                },
                onSelect = {
                    onSelect(it)
                },
                onAddToFavorite = {
                    onAddToFavorite(it)
                },
                onRemoveFromFavorite = {
                    onRemoveFromFavorite(it)
                },
                isSelectionMode = files.any { file -> file.isSelected }
            )
        }
    }
}