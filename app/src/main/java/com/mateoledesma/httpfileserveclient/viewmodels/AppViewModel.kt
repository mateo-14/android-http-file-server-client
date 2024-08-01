package com.mateoledesma.httpfileserveclient.viewmodels

import androidx.lifecycle.ViewModel
import com.mateoledesma.httpfileserveclient.data.FavoritesFilesRepository
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val favoritesFilesRepository: FavoritesFilesRepository): ViewModel() {

    suspend fun addFileToFavorites(file: FileEntry) {
        favoritesFilesRepository.addFile(file)
    }
}