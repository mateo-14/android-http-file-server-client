package com.mateoledesma.httpfileserveclient.utils

import com.mateoledesma.httpfileserveclient.data.SortBy
import com.mateoledesma.httpfileserveclient.data.model.FileEntry

fun sortFiles(files: List<FileEntry>, isSortAscending: Boolean, sortBy: SortBy): List<FileEntry> {
    if (isSortAscending) {
        return files.sortedWith { a, b ->
            when {
                a.isDirectory && !b.isDirectory -> -1
                !a.isDirectory && b.isDirectory -> 1
                else -> {
                    when (sortBy) {
                        SortBy.NAME -> a.name.compareTo(b.name)
                        SortBy.DATE -> a.updatedAt.compareTo(b.updatedAt)
                        SortBy.SIZE -> a.size.compareTo(b.size)
                        SortBy.TYPE -> {
                            if (!a.isDirectory) {
                                val extA = a.name.substringAfterLast('.')
                                val extB = b.name.substringAfterLast('.')
                                extA.compareTo(extB)
                            } else {
                                0
                            }
                        }
                    }
                }
            }
        }
    } else {
        return files.sortedWith { a, b ->
            when {
                a.isDirectory && !b.isDirectory -> -1
                !a.isDirectory && b.isDirectory -> 1
                else -> {
                    when (sortBy) {
                        SortBy.NAME -> b.name.compareTo(a.name)
                        SortBy.DATE -> b.updatedAt.compareTo(a.updatedAt)
                        SortBy.SIZE -> b.size.compareTo(a.size)
                        SortBy.TYPE -> {
                            if (!a.isDirectory) {
                                val extA = a.name.substringAfterLast('.')
                                val extB = b.name.substringAfterLast('.')
                                extB.compareTo(extA)
                            } else {
                                0
                            }
                        }
                    }
                }
            }
        }
    }
}
