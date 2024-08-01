package com.mateoledesma.httpfileserveclient.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
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

    data object Favorites : Screen("favorites")
}
