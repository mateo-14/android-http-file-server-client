package com.mateoledesma.httpfileserveclient.data

import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.data.network.FileServerApi
import javax.inject.Inject

class FileServerRepository @Inject() constructor(
    private val api: FileServerApi
) {
    suspend fun getFiles(path: String): List<FileEntry> {
        return api.getFiles(path)
    }
}