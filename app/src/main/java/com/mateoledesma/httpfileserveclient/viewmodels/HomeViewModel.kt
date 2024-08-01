package com.mateoledesma.httpfileserveclient.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mateoledesma.httpfileserveclient.data.FavoritesFilesRepository
import com.mateoledesma.httpfileserveclient.data.FileServerRepository
import com.mateoledesma.httpfileserveclient.data.UserPreferencesRepository
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UiState(
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

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    val isLinearLayout: StateFlow<Boolean> = userPreferencesRepository.isLinearLayout.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    suspend fun getFiles(path: String): Boolean {
        try {
            _uiState.value = UiState(isLoading = true)
            val files = fileServerRepository.getFiles(path)
            val favoriteFiles = favoritesFilesRepository.getFilesByIds(files.map { it.id })
            _uiState.value = _uiState.value.copy(
                files = files.map { file ->
                    file.copy(isFavorite = favoriteFiles.contains(file.id))
                },
                isLoading = false
            )
            return true
        } catch (e: Exception) {
            _uiState.value = UiState(isLoading = false, hasError = true)
            return false
        }
    }

    fun saveLayoutPreference(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    fun selectFile(file: FileEntry) {
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
                if (it == file) {
                    it.copy(isSelected = true)
                } else {
                    it
                }
            }
        )
    }

    fun unselectFile(file: FileEntry) {
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
                if (it == file) {
                    it.copy(isSelected = false)
                } else {
                    it
                }
            }
        )
    }

    fun clearSelectedFiles() {
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
                it.copy(isSelected = false)
            }
        )
    }

    suspend fun addFileToFavorites(file: FileEntry) {
        favoritesFilesRepository.addFile(file)
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
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
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
                if (it == file) {
                    it.copy(isFavorite = false)
                } else {
                    it
                }
            }
        )
    }

    fun hasSelectedFiles(): Boolean {
        return _uiState.value.files.any { it.isSelected }
    }

    fun selectAllFiles() {
        _uiState.value = _uiState.value.copy(
            files = _uiState.value.files.map {
                it.copy(isSelected = true)
            }
        )
    }

    fun addFilesToFavorites(files: List<FileEntry>) {
        viewModelScope.launch {
            favoritesFilesRepository.addFiles(files)
            _uiState.value = _uiState.value.copy(
                files = _uiState.value.files.map {
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
            _uiState.value = _uiState.value.copy(
                files = _uiState.value.files.map {
                    if (files.contains(it)) {
                        it.copy(isFavorite = false)
                    } else {
                        it
                    }
                }
            )
        }
    }
}