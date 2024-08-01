package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectedFilesTopBarIndicator(
    modifier: Modifier = Modifier,
    count: Int,
    onClearSelectedFiles: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClearSelectedFiles() }) {
            Icon(Icons.Rounded.Close, contentDescription = "Clear Selected Files")
        }
        Text(
            modifier = Modifier
                .padding(start = 6.dp)
                .paddingFromBaseline(bottom = 9.dp),
            text = count.toString(),
            fontSize = 20.sp
        )
    }
}