package com.mateoledesma.httpfileserveclient.ui.screens.home.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.Screen
import com.mateoledesma.httpfileserveclient.ui.components.FilesOptionsDropdownMenu
import com.mateoledesma.httpfileserveclient.ui.components.SearchTopBar
import com.mateoledesma.httpfileserveclient.ui.components.SelectedFilesTopBarIndicator
import com.mateoledesma.httpfileserveclient.ui.components.SortDropdownMenu

// TODO Move navController logic to HomeScreen
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClearSelectedFiles: () -> Unit,
    onChangeLayout: (Boolean) -> Unit,
    onSelectAll: () -> Unit,
    onAddToFavorites: () -> Unit,
    onRemoveFromFavorites: () -> Unit,
    onRandomFile: () -> Unit,
    selectedFiles: List<FileEntry>,
    isLinearLayout: Boolean,
    path: String,
    onSearchValueChange: (String) -> Unit,
    searchValue: String,
) {
    val hasSelectedFiles = selectedFiles.isNotEmpty()
    val pathList = listOf("Home") + path.split("/").filter { it.isNotBlank() }
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Color transition
    val topAppBarColors = TopAppBarDefaults.topAppBarColors()
    val colorTransitionFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    val appBarContainerColor by animateColorAsState(
        targetValue = lerp(
            topAppBarColors.containerColor,
            topAppBarColors.scrolledContainerColor,
            fraction
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "appBarContainerColor"
    )

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

    Column(modifier = modifier) {
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
                        onClearSelectedFiles = onClearSelectedFiles
                    )
                }
            },
            title = {
                if (!hasSelectedFiles && !isSearching) {
                    Text("Server Files")
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
                        onChangeLayout = onChangeLayout
                    )
                }
                FilesOptionsDropdownMenu(
                    onSelectAll = onSelectAll,
                    onAddToFavorites = if (hasSelectedFiles) onAddToFavorites else null,
                    onRemoveFromFavorites = if (hasSelectedFiles) onRemoveFromFavorites else null,
                    onRandomFile = onRandomFile
                )
            }
        )
        PathBreadcrumb(
            pathList = pathList,
            firstItemPadding = PaddingValues(start = 5.5.dp),
            modifier = Modifier
                .background(appBarContainerColor)
                .padding(
                    vertical = 4.dp
                )
                .fillMaxWidth(),
            onItemClick = { index ->
                if (index == 0) {
                    navController.popBackStack(
                        Screen.Home.createRoute(),
                        false
                    )
                } else {
                    navController.popBackStack(
                        Screen.Home.createRoute(
                            pathList.subList(1, index + 1).joinToString("/")
                        ),
                        false
                    )
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
suspend fun TopAppBarScrollBehavior.expandAnimating() {
    AnimationState(
        initialValue = this.state.heightOffset
    )
        .animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        ) { this@expandAnimating.state.heightOffset = value }
}