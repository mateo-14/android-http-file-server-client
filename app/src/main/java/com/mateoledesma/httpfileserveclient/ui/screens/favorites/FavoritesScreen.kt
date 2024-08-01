package com.mateoledesma.httpfileserveclient.ui.screens.favorites

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
    navController: NavController,
    onClickFile: (FileEntry) -> Unit,
    onRandomFile: (FileEntry) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel = hiltViewModel<FavoritesViewModel>()
    val isLinearLayout by viewModel.isLinearLayout.collectAsState()
    val files by viewModel.files.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val selectedFiles = files.filter { it.isSelected }

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
                isLinearLayout = isLinearLayout,
                onLayoutChange = { isLinearLayout ->
                    viewModel.saveLayoutPreference(isLinearLayout)
                },
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
                        if (files.isNotEmpty()) {
                            onRandomFile(files.random())
                        } else {
                            Toast.makeText(
                                navController.context,
                                "No files to open",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
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
                    files = files,
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
                )
            } else {
                FileGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp
                        ),
                    files = files,
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
                )
            }
        }
    }
}