package com.mateoledesma.httpfileserveclient.utils

import java.text.NumberFormat

fun formatBytes(bytes: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var bytesFloat = bytes.toFloat()
    var unit = 0
    while (bytesFloat > 1024) {
        bytesFloat /= 1024
        unit++
    }

    val numberFormat = NumberFormat.getNumberInstance()
    numberFormat.maximumFractionDigits = 2
    return "${numberFormat.format(bytesFloat)} ${units[unit]}"
}