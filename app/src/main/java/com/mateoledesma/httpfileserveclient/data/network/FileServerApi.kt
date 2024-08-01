package com.mateoledesma.httpfileserveclient.data.network

import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface FileServerApi {
    @GET("explore")
    suspend fun getFiles(@Query("path") path: String): List<FileEntry>

}