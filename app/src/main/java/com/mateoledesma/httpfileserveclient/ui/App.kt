package com.mateoledesma.httpfileserveclient.ui

import android.content.Intent
import android.net.Uri
import android.os.Debug
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mateoledesma.httpfileserveclient.data.model.FileEntry
import com.mateoledesma.httpfileserveclient.ui.components.NavigationDrawerContent
import com.mateoledesma.httpfileserveclient.ui.components.RandomFileDialog
import com.mateoledesma.httpfileserveclient.ui.screens.favorites.FavoritesScreen
import com.mateoledesma.httpfileserveclient.ui.screens.home.HomeScreen
import com.mateoledesma.httpfileserveclient.viewmodels.AppViewModel
import kotlinx.coroutines.launch
import java.net.URLDecoder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                drawerState = drawerState
            )
        },
    ) {
        AppNavHost(navController = navController)
    }

}

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    var randomFile by remember { mutableStateOf<FileEntry?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val viewmodel = hiltViewModel<AppViewModel>()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    fun onClickFile(file: FileEntry) {
        if (file.isDirectory) {
            navController.navigate(
                Screen.Home.createRoute(
                    file.path
                )
            )
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            if (file.url.endsWith(".txt")) {
                intent.setDataAndType(Uri.parse(file.url), "text/plain")
            } else {
                intent.setDataAndType(Uri.parse(file.url), file.mimeType)
            }

            try {
                navController.context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("HomeScreen", "Error opening file", e)
            }
        }
    }

    fun onRandomFile(file: FileEntry) {
        randomFile = file
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.createRoute(),
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            ) + slideOutOfContainer(
                animationSpec = tween(300, easing = EaseOut),
                towards = AnimatedContentTransitionScope.SlideDirection.End
            )
        }
    ) {
        composable(
            route = Screen.Home.route,
            arguments = Screen.Home.navArguments
        ) { backStackEntry ->
            HomeScreen(
                path = URLDecoder.decode(
                    backStackEntry.arguments?.getString("path")!!,
                    "UTF-8"
                ),
                navController = navController,
                onClickFile = ::onClickFile,
                onRandomFile = ::onRandomFile
            )
        }
        composable(
            route = Screen.Favorites.route
        ) {
            FavoritesScreen(
                navController = navController,
                onClickFile = ::onClickFile,
                onRandomFile = ::onRandomFile
            )
        }
    }

    if (randomFile != null) {
        RandomFileDialog(
            file = randomFile!!,
            onDismissRequest = {
                randomFile = null
            },
            openFile = {
                onClickFile(randomFile!!)
            },
            addToFavorite = if (currentRoute == Screen.Favorites.route) null else {
                {
                    coroutineScope.launch {
                        viewmodel.addFileToFavorites(randomFile!!)
                        Toast.makeText(
                            navController.context,
                            "File added to favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )
    }
}