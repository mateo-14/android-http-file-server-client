package com.mateoledesma.httpfileserveclient.ui.screens.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.components.FileGrid
import com.mateoledesma.httpfileserveclient.ui.components.FileList
import com.mateoledesma.httpfileserveclient.ui.components.PullToRefresh
import com.mateoledesma.httpfileserveclient.ui.screens.home.components.HomeTopBar
import com.mateoledesma.httpfileserveclient.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    path: String,
    navController: NavController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    onClickFile: (FileEntry) -> Unit,
    onRandomFile: (FileEntry) -> Unit,
) {
    val filesState by viewModel.filesState.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    val isLinearLayout by viewModel.isLinearLayout.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val filteredFiles by viewModel.filteredFiles.collectAsState()
    val selectedFiles = filteredFiles.filter { it.isSelected }
    val scrollBehavior =
        if (filesState.files.isNotEmpty()) TopAppBarDefaults.enterAlwaysScrollBehavior() else TopAppBarDefaults.pinnedScrollBehavior()


    LaunchedEffect(key1 = true) {
        val success = viewModel.getFiles(path)
        if (!success) {
            Toast.makeText(navController.context, "Failed to load files", Toast.LENGTH_SHORT).show()
        }
    }

    BackHandler(enabled = viewModel.hasSelectedFiles()) {
        viewModel.clearSelectedFiles()
    }

    fun onSelectFile(file: FileEntry) {
        if (file.isSelected) {
            viewModel.unselectFile(file)
        } else {
            viewModel.selectFile(file)
        }
    }

    fun onAddFileToFavorites(file: FileEntry) {
        coroutineScope.launch {
            viewModel.addFileToFavorites(file)
        }
    }

    fun onRemoveFileFromFavorites(file: FileEntry) {
        coroutineScope.launch {
            viewModel.removeFileFromFavorites(file)
        }
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                isLinearLayout = isLinearLayout,
                path = path,
                selectedFiles = selectedFiles,
                onClearSelectedFiles = { viewModel.clearSelectedFiles() },
                onChangeLayout = { isLinearLayout ->
                    viewModel.saveLayoutPreference(isLinearLayout)
                },
                onSelectAll = {
                    viewModel.selectAllFiles()
                },
                onAddToFavorites =  {
                    coroutineScope.launch {
                        viewModel.addFilesToFavorites(selectedFiles)
                    }
                },
                onRemoveFromFavorites = {
                    coroutineScope.launch {
                        viewModel.removeFilesFromFavorites(selectedFiles)
                    }
                },
                onRandomFile = {
                    coroutineScope.launch {
                        if (filteredFiles.isNotEmpty()) {
                            onRandomFile(filteredFiles.random())
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
                searchValue = viewModel.searchValue.collectAsState().value
            )
        }) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            if (filesState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                if (isLinearLayout) {
                    FileList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp
                            ),
                        files = filteredFiles,
                        onSelect = ::onSelectFile,
                        onAddToFavorite = ::onAddFileToFavorites,
                        onRemoveFromFavorite = ::onRemoveFileFromFavorites,
                        onClickFile = onClickFile
                    )
                } else {
                    FileGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp
                            ),
                        files = filteredFiles,
                        onSelect = ::onSelectFile,
                        onAddToFavorite = ::onAddFileToFavorites,
                        onRemoveFromFavorite = ::onRemoveFileFromFavorites,
                        onClickFile = onClickFile
                    )
                }
            }

            LaunchedEffect(filesState.isLoading) {
                if (!filesState.isLoading) {
                    pullToRefreshState.endRefresh()
                }
            }

            if (!filesState.hasError) {
                PullToRefresh(modifier = Modifier.align(Alignment.TopCenter),
                    pullToRefreshState = pullToRefreshState,
                    onRefresh = {
                        coroutineScope.launch {
                            val success = viewModel.getFiles(path)
                            if (!success) {
                                Toast.makeText(
                                    navController.context,
                                    "Failed to load files",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            viewModel.getFiles(path)
                        }
                    }) {
                        Text(text = "Retry")
                    }
                }
            }
        }
    }
}
