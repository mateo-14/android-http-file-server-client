package com.mateoledesma.httpfileserveclient.utils

import com.mateoledesma.httpfileserveclient.data.model.FileEntry

fun getThumbnailUrl(file: FileEntry): String? {
    if (file.thumbnail != null) {
        return file.thumbnail
    }

    if (file.mimeType?.contains("image") == true) {
        return file.url
    }

    return null
}
