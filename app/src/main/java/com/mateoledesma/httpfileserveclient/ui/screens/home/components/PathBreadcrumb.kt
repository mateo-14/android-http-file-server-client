package com.mateoledesma.httpfileserveclient.ui.screens.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun PathBreadcrumb(
    modifier: Modifier = Modifier,
    firstItemPadding: PaddingValues = PaddingValues(start = 8.dp),
    lastItemPadding: PaddingValues = PaddingValues(end = 8.dp),
    pathList: List<String>,
    onItemClick: (Int) -> Unit
) {
    val lazyState = rememberLazyListState()

    LaunchedEffect(key1 = pathList.count()) {
        lazyState.animateScrollToItem(pathList.size - 1)
    }

    LazyRow(
        modifier = modifier,
        state = lazyState
    ) {
        itemsIndexed(pathList) { index, folder ->
            BreadcrumbItem(
                modifier = Modifier
                    .padding(
                        when (index) {
                            0 -> firstItemPadding
                            pathList.size - 1 -> lastItemPadding
                            else -> PaddingValues(
                                horizontal = 2.dp
                            )
                        }
                    ),
                text = folder,
                onClick = {
                    onItemClick(index)
                },
                showArrow = index < pathList.size - 1,
                isSelected = index == pathList.size - 1
            )
        }
    }
}

@Preview
@Composable
fun PathBreadcrumbPreview() {
    PathBreadcrumb(
        pathList = listOf("Home", "Folder 1", "Folder 2", "Folder 3"),
        onItemClick = {
        }
    )
}