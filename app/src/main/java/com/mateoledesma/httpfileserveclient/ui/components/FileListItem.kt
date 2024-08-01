package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileListItem(
    modifier: Modifier = Modifier,
    name: String,
    isDirectory: Boolean = false,
    thumbnail: String? = null,
    onClick: () -> Unit,
    onSelect: () -> Unit,
    isSelectionMode: Boolean,
    onAddToFavorite: () -> Unit = {},
    onRemoveFromFavorite: () -> Unit = {},
    selected: Boolean = false,
    isFavorite: Boolean = false,
) {

    Row(
        modifier = modifier
            .clip(
                MaterialTheme.shapes.medium
            )
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        onSelect()
                    } else {
                        onClick()
                    }
                },
                onLongClick = onSelect,
            )
            .background(
                if (selected) Color.White.copy(alpha = 0.14f) else Color.Transparent
            )
            .padding(vertical = 8.dp)
            .padding(start = 12.dp, end = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(
                    end = 20.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            if (thumbnail.isNullOrEmpty()) {
                Icon(
                    if (isDirectory)
                        Icons.Outlined.Folder else
                        Icons.Outlined.Description,
                    contentDescription = "Folder",
                    modifier = Modifier
                        .size(32.dp)
                )
            } else {
                AsyncImage(
                    model = thumbnail,
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                )
            }

            if (selected) {
                Icon(
                    modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .size(18.dp)
                        .offset(
                            x = (-6).dp,
                            y = (-12).dp
                        ),
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isFavorite) {
                Icon(
                    modifier = Modifier
                        .align(
                            Alignment.TopStart
                        )
                        .size(18.dp),
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }

        Text(
            modifier = Modifier.weight(1f),
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        FileOptionsDropdownMenu(
            modifier = Modifier.align(Alignment.CenterVertically),
            isFavorite = isFavorite,
            onAddToFavorite = onAddToFavorite,
            onRemoveFromFavorite = onRemoveFromFavorite
        )
    }
}

@Preview
@Composable
fun FileListItemPreview() {
    FileListItem(
        modifier = Modifier.fillMaxWidth(),
        name = "File",
        isDirectory = false,
        selected = true,
        onClick = {},
        onSelect = {},
        isSelectionMode = false
    )
}