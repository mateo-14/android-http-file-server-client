package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefresh(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    pullToRefreshState: PullToRefreshState,
) {
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(key1 = true) {
            onRefresh()
        }
    }

    PullToRefreshContainer(
        state = pullToRefreshState,
        modifier = modifier
    )
}
