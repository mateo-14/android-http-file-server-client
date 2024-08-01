package com.mateoledesma.httpfileserveclient.data.model

import com.mateoledesma.httpfileserveclient.data.database.entities.FavoriteFileEntity
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class FileEntry(
    val id: Long,
    val name: String,
    val isDirectory: Boolean,
    val size: Long,
    val mimeType: String? = null,
    val path: String,
    val thumbnail: String? = null,
    val updatedAt: Long,
    val url: String,
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false
)

fun FileEntry.toEntity() = FavoriteFileEntity(
    id = id,
    name = name,
    path = path,
    isDirectory = isDirectory,
    size = size,
    lastModified = updatedAt,
    url = url,
    mimeType = mimeType,
    thumbnail = thumbnail,
    addedAt = Date(),
)