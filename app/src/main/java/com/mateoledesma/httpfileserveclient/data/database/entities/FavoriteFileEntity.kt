package com.mateoledesma.httpfileserveclient.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import java.util.Date

@Entity(tableName = "favorites_files")
data class FavoriteFileEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long,
    val url: String,
    val mimeType: String?,
    val thumbnail: String?,
    val addedAt: Date
)
fun FavoriteFileEntity.toModel() = FileEntry(
    id = id,
    name = name,
    path = path,
    isDirectory = isDirectory,
    size = size,
    updatedAt = lastModified,
    url = url,
    mimeType = mimeType,
    thumbnail = thumbnail,
    isFavorite = true
)