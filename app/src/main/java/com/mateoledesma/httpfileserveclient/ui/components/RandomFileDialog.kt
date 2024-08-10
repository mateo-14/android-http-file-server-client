package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.utils.formatBytes
import com.mateoledesma.httpfileserveclient.utils.getThumbnailUrl
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date

@Composable
fun RandomFileDialog(
    file: FileEntry,
    onDismissRequest: () -> Unit,
    openFile: () -> Unit,
    addToFavorite: (() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val thumbnail = getThumbnailUrl(file)
                if (!thumbnail.isNullOrEmpty()) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(
                                MaterialTheme.shapes.small
                            ),
                        model = thumbnail,
                        contentDescription = file.name,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        if (file.isDirectory)
                            Icons.Rounded.Folder else
                            Icons.Rounded.Description,
                        contentDescription = "Folder",
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 12.dp, start = 6.dp, end = 6.dp),
                    text = file.name,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                FilePropertyItem(
                    modifier = Modifier.padding(top = 16.dp),
                    label = "Path",
                    value = file.path
                )

                val numberFormat = NumberFormat.getNumberInstance()
                FilePropertyItem(
                    modifier = Modifier.padding(top = 18.dp),
                    label = "Size",
                    value = "${formatBytes(file.size)} (${numberFormat.format(file.size)} bytes)"
                )

                val dateFormat = DateFormat.getDateTimeInstance()
                FilePropertyItem(
                    modifier = Modifier.padding(top = 18.dp),
                    label = "Updated At",
                    value = dateFormat.format(Date(file.updatedAt * 1000))
                )

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = if (addToFavorite != null) 12.dp else 0.dp),
                        onClick = openFile
                    ) {
                        Text(text = "Open")
                    }
                    if (addToFavorite != null) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp),
                            onClick = addToFavorite
                        ) {
                            Text(text = "Add to favorite")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilePropertyItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
        Text(
            text = value,
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}