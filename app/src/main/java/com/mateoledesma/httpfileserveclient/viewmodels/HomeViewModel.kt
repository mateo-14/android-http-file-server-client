package com.mateoledesma.httpfileserveclient.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateoledesma.httpfileserveclient.data.FavoritesFilesRepository
import com.mateoledesma.httpfileserveclient.data.FileServerRepository
import com.mateoledesma.httpfileserveclient.data.UserPreferencesRepository
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestState(
    val isLoading: Boolean = true,
    val files: List<FileEntry> = emptyList(),
    val hasError: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fileServerRepository: FileServerRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val favoritesFilesRepository: FavoritesFilesRepository,
) :
    ViewModel() {

    private val _filesState = MutableStateFlow(RequestState())
    val filesState: StateFlow<RequestState> = _filesState.asStateFlow()

    val isLinearLayout: StateFlow<Boolean> = userPreferencesRepository.isLinearLayout.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    private val _searchValue = MutableStateFlow("")
    val searchValue: StateFlow<String> = _searchValue.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val filteredFiles: StateFlow<List<FileEntry>> = _searchValue.debounce(300).flatMapLatest { searchValue ->
        _filesState.map { filesState ->
            filesState.files.filter { file ->
                file.name.contains(searchValue, ignoreCase = true)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        emptyList()
    )


    suspend fun getFiles(path: String): Boolean {
        try {
            _filesState.value = RequestState(isLoading = true)
            val files = fileServerRepository.getFiles(path)
            val favoriteFiles = favoritesFilesRepository.getFilesByIds(files.map { it.id })
            _filesState.value = _filesState.value.copy(
                files = files.map { file ->
                    file.copy(isFavorite = favoriteFiles.contains(file.id))
                },
                isLoading = false
            )
            return true
        } catch (e: Exception) {
            _filesState.value = RequestState(isLoading = false, hasError = true)
            return false
        }
    }

    fun saveLayoutPreference(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    fun selectFile(file: FileEntry) {
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                if (it == file) {
                    it.copy(isSelected = true)
                } else {
                    it
                }
            }
        )
    }

    fun unselectFile(file: FileEntry) {
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                if (it == file) {
                    it.copy(isSelected = false)
                } else {
                    it
                }
            }
        )
    }

    fun clearSelectedFiles() {
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                it.copy(isSelected = false)
            }
        )
    }

    suspend fun addFileToFavorites(file: FileEntry) {
        favoritesFilesRepository.addFile(file)
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                if (it == file) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }
        )
    }

    suspend fun removeFileFromFavorites(file: FileEntry) {
        favoritesFilesRepository.deleteFile(file)
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                if (it == file) {
                    it.copy(isFavorite = false)
                } else {
                    it
                }
            }
        )
    }

    fun hasSelectedFiles(): Boolean {
        return _filesState.value.files.any { it.isSelected }
    }

    fun selectAllFiles() {
        _filesState.value = _filesState.value.copy(
            files = _filesState.value.files.map {
                it.copy(isSelected = true)
            }
        )
    }

    fun addFilesToFavorites(files: List<FileEntry>) {
        viewModelScope.launch {
            favoritesFilesRepository.addFiles(files)
            _filesState.value = _filesState.value.copy(
                files = _filesState.value.files.map {
                    if (files.contains(it)) {
                        it.copy(isFavorite = true)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun removeFilesFromFavorites(files: List<FileEntry>) {
        viewModelScope.launch {
            favoritesFilesRepository.deleteFiles(files)
            _filesState.value = _filesState.value.copy(
                files = _filesState.value.files.map {
                    if (files.contains(it)) {
                        it.copy(isFavorite = false)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun searchFiles(searchValue: String) {
        _searchValue.value = searchValue
    }
}