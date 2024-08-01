package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mateoledesma.httpfileserveclient.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawerContent(navController: NavController, drawerState: DrawerState) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(12.dp))
            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = {
                    Icon(
                        Icons.Outlined.Storage,
                        contentDescription = "Server Files"
                    )
                },
                label = { Text("Files") },
                selected = currentBackStackEntry?.destination?.route == Screen.Home.route,
                onClick = {
                    if (currentBackStackEntry?.destination?.route != Screen.Home.route) {
                        navController.popBackStack()
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                },
            )
            NavigationDrawerItem(
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorites"
                    )
                },
                label = { Text("Favorites") },
                selected = currentBackStackEntry?.destination?.route == Screen.Favorites.route,
                onClick = {
                    if (currentBackStackEntry?.destination?.route != Screen.Favorites.route) {
                        navController.navigate(Screen.Favorites.route)
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                },
            )
//            NavigationDrawerItem(
//                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
//                icon = {
//                    Icon(
//                        Icons.Outlined.Shuffle,
//                        contentDescription = "Random file"
//                    )
//                },
//                label = { Text("Random file") },
//                selected = false,
//                onClick = {
//                    if (currentBackStackEntry?.destination?.route != Screen.RandomFile.route) {
//                        navController.navigate(Screen.RandomFile.createRoute())
//                        coroutineScope.launch {
//                            drawerState.close()
//                        }
//                    }
//                },
//            )
        }
    }
}