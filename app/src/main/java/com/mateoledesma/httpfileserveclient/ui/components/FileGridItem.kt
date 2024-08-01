package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileGridItem(
    modifier: Modifier = Modifier,
    name: String,
    isDirectory: Boolean = false,
    thumbnail: String? = null,
    onClick: () -> Unit,
    onSelect: () -> Unit,
    selected: Boolean = false,
    isFavorite: Boolean = false,
    isSelectionMode: Boolean,
    onAddToFavorite: () -> Unit,
    onRemoveFromFavorite: () -> Unit,
) {
    Column(
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
                onLongClick = {
                    onSelect()
                },
            )
            .background(
                if (selected) Color.White.copy(alpha = 0.14f) else Color.Transparent
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            contentAlignment = Alignment.Center
        ) {
            if (thumbnail.isNullOrEmpty()) {
                Icon(
                    if (isDirectory)
                        Icons.Rounded.Folder else
                        Icons.Rounded.Description,
                    contentDescription = "Folder",
                    modifier = Modifier
                        .size(80.dp)
                )
            } else {
                AsyncImage(
                    model = thumbnail,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isFavorite) {
                Icon(
                    modifier = Modifier
                        .align(
                            Alignment.TopStart
                        )
                        .padding(
                            start = 6.dp,
                            top = 6.dp
                        )
                        .size(20.dp),
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Color.Red
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(start = 12.dp, top = 12.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
//                    onTextLayout = {
//                        val lineCount = it.lineCount
//                        val height = (it.size.height / density).dp
//                        padding = if (lineCount > 1) 0.dp else height
//                    }
                )

                FileOptionsDropdownMenu(
                    isFavorite = isFavorite,
                    onAddToFavorite = onAddToFavorite,
                    onRemoveFromFavorite = onRemoveFromFavorite
                )
            }

            if (selected) {
                Icon(
                    modifier = Modifier
                        .align(
                            Alignment.BottomEnd
                        )
                        .padding(
                            bottom = 6.dp,
                            end = 6.dp
                        )
                        .size(20.dp),
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}