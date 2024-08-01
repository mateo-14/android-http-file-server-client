package com.mateoledesma.httpfileserveclient.data

import com.mateoledesma.httpfileserveclient.data.database.dao.FavoriteFileDao
import com.mateoledesma.httpfileserveclient.data.database.entities.toModel
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.data.model.toEntity
import javax.inject.Inject

class FavoritesFilesRepository @Inject constructor(
    private val filesDao: FavoriteFileDao,
) {
    suspend fun getAllFiles(): List<FileEntry> {
        return filesDao.getAll().map {
            it.toModel()
        }
    }

    suspend fun addFile(file: FileEntry) {
        filesDao.insert(file.toEntity())
    }

    suspend fun deleteFile(file: FileEntry) {
        filesDao.delete(file.toEntity())
    }

    suspend fun getFilesByIds(ids: List<Long>): List<Long> {
        return filesDao.getIdsByIds(ids)
    }

    suspend fun addFiles(files: List<FileEntry>) {
        filesDao.insertMany(files.map { it.toEntity() })
    }

    suspend fun deleteFiles(files: List<FileEntry>) {
        filesDao.deleteMany(files.map { it.toEntity() })
    }
}