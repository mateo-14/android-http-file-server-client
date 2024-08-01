package com.mateoledesma.httpfileserveclient.ui

import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList(),
) {
    data object Home : Screen(
        route = "home?path={path}",
        navArguments = listOf(
            navArgument("path") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) {
        fun createRoute(path: String = ""): String {
            return "home?path=${URLEncoder.encode(path, "UTF-8")}"
        }
    }

    data object RandomFile : Screen(route = "random_file?file={file}",
        navArguments = listOf(
            navArgument("file") {
                type = NavType.StringType
                defaultValue = ""
            }
        )) {
        fun createRoute(file: FileEntry? = null): String {
            if (file == null) {
                return route
            }

            val jsonFile = Json.encodeToString(file)
            return "random_file?file=${URLEncoder.encode(jsonFile, "UTF-8")}"
        }
    }

    data object Favorites : Screen("favorites") {
        fun createRoute(): String {
            return route
        }
    }
}
