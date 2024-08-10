package com.mateoledesma.httpfileserveclient.ui.screens.favorites

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.components.FileGrid
import com.mateoledesma.httpfileserveclient.ui.components.FileList
import com.mateoledesma.httpfileserveclient.ui.screens.favorites.components.FavoritesTopBar
import com.mateoledesma.httpfileserveclient.viewmodels.FavoritesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel<FavoritesViewModel>(),
    navController: NavController,
    onClickFile: (FileEntry) -> Unit,
    onRandomFile: (FileEntry) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    val filteredAndSortedFiles by viewModel.filteredAndSortedFiles.collectAsState()
    val isLinearLayout by viewModel.isLinearLayout.collectAsState()
    val isSortAscending by viewModel.isSortAscending.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()

    val selectedFiles = filteredAndSortedFiles.filter { it.isSelected }


    BackHandler(enabled = viewModel.hasSelectedFiles()) {
        viewModel.clearSelectedFiles()
    }

    LaunchedEffect(key1 = sortBy, key2 = isSortAscending, key3 = isLinearLayout) {
        listState.scrollToItem(0)
        gridState.scrollToItem(0)
    }

    fun handleSelect(file: FileEntry) {
        if (file.isSelected) {
            viewModel.unselectFile(file)
        } else {
            viewModel.selectFile(file)
        }
    }

    fun onRemoveFileFromFavorites(file: FileEntry) {
        coroutineScope.launch {
            viewModel.removeFromFavorites(file)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FavoritesTopBar(
                scrollBehavior = scrollBehavior,
                selectedFiles = selectedFiles,
                onClearSelectedFiles = {
                    viewModel.clearSelectedFiles()
                },
                onSelectAll = {
                    viewModel.selectAllFiles()
                },
                onRemoveFromFavorites = {
                    viewModel.removeFilesFromFavorites(selectedFiles)
                },
                onRandomFile = {
                    coroutineScope.launch {
                        if (filteredAndSortedFiles.isNotEmpty()) {
                            onRandomFile(filteredAndSortedFiles.random())
                        } else {
                            Toast.makeText(
                                navController.context,
                                "No files to open",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onSearchValueChange = { searchValue ->
                    viewModel.searchFiles(searchValue)
                },
                searchValue =  viewModel.searchValue.collectAsState().value,
                isLinearLayout = isLinearLayout,
                onChangeLayout = { isLinearLayout ->
                    viewModel.saveLayoutPreference(isLinearLayout)
                },
                isSortAscending =  isSortAscending,
                onChangeSortAscending = { isSortAscending ->
                    viewModel.saveSortAscendingPreference(isSortAscending)
                },
                sortBy = sortBy,
                onChangeSortMode = { sortBy ->
                    viewModel.saveSortByPreference(sortBy)
                }
            )
        }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLinearLayout) {
                FileList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp
                        ),
                    files = filteredAndSortedFiles,
                    onSelect = { file ->
                        handleSelect(file)
                    },
                    onAddToFavorite = { },
                    onRemoveFromFavorite = { file ->
                        onRemoveFileFromFavorites(file)
                    },
                    onClickFile = { file ->
                        onClickFile(file)
                    },
                    state = listState
                )
            } else {
                FileGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp
                        ),
                    files = filteredAndSortedFiles,
                    onSelect = { file ->
                        handleSelect(file)
                    },
                    onAddToFavorite = { },
                    onRemoveFromFavorite = { file ->
                        onRemoveFileFromFavorites(file)
                    },
                    onClickFile = { file ->
                        onClickFile(file)
                    },
                    state = gridState
                )
            }
        }
    }
}