package com.mateoledesma.httpfileserveclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateoledesma.httpfileserveclient.data.FavoritesFilesRepository
import com.mateoledesma.httpfileserveclient.data.SortBy
import com.mateoledesma.httpfileserveclient.data.UserPreferencesRepository
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.utils.sortFiles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesFilesRepository: FavoritesFilesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    private val _files = MutableStateFlow<List<FileEntry>>(emptyList())

    val isLinearLayout: StateFlow<Boolean> = userPreferencesRepository.isLinearLayout.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val isSortAscending: StateFlow<Boolean> = userPreferencesRepository.isSortAscending.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    val sortBy: StateFlow<SortBy> = userPreferencesRepository.sortBy.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SortBy.NAME
    )

    private val _searchValue = MutableStateFlow("")
    val searchValue: StateFlow<String> = _searchValue.asStateFlow()

    val filteredAndSortedFiles: StateFlow<List<FileEntry>> = combine(
        _files,
        _searchValue,
        isSortAscending,
        sortBy
    ) { files, searchValue, isSortAscending, sortBy ->
        val filteredFiles = files.filter { file ->
            file.name.contains(searchValue, ignoreCase = true)
        }

        sortFiles(filteredFiles, isSortAscending, sortBy)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )

    init {
        viewModelScope.launch {
            _files.value = favoritesFilesRepository.getAllFiles()
        }
    }

    suspend fun removeFromFavorites(file: FileEntry) {
        favoritesFilesRepository.deleteFile(file)
        _files.value = _files.value.filter { it.id != file.id }
    }

    fun saveLayoutPreference(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    fun saveSortAscendingPreference(isSortAscending: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveSortAscendingPreference(isSortAscending)
        }
    }

    fun saveSortByPreference(sortBy: SortBy) {
        viewModelScope.launch {
            userPreferencesRepository.saveSortByPreference(sortBy)
        }
    }

    fun selectFile(file: FileEntry) {
        _files.value = _files.value.map {
            if (it.id == file.id) {
                it.copy(isSelected = !it.isSelected)
            } else {
                it
            }
        }
    }

    fun unselectFile(file: FileEntry) {
        _files.value = _files.value.map {
            if (it.id == file.id) {
                it.copy(isSelected = false)
            } else {
                it
            }
        }
    }

    fun clearSelectedFiles() {
        _files.value = _files.value.map {
            it.copy(isSelected = false)
        }
    }

    fun selectAllFiles() {
        _files.value = _files.value.map {
            it.copy(isSelected = true)
        }
    }

    fun removeFilesFromFavorites(selectedFiles: List<FileEntry>) {
        viewModelScope.launch {
            selectedFiles.forEach { file ->
                favoritesFilesRepository.deleteFile(file)
            }
            _files.value = _files.value.filter { file -> !selectedFiles.contains(file) }
        }
    }

    fun searchFiles(searchValue: String) {
        _searchValue.value = searchValue
    }

    fun hasSelectedFiles(): Boolean {
        return _files.value.any { it.isSelected }
    }
}