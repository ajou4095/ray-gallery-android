package com.ray.gallery.android.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ray.gallery.android.presentation.common.theme.GalleryTheme
import com.ray.gallery.android.presentation.common.util.compose.ErrorObserver
import com.ray.gallery.android.presentation.ui.main.home.HomeConstant
import com.ray.gallery.android.presentation.ui.main.home.homeDestination

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    GalleryTheme {
        val navController = rememberNavController()

        ErrorObserver(viewModel)

        NavHost(
            navController = navController,
            startDestination = HomeConstant.ROUTE
        ) {
            homeDestination(navController = navController)
        }
    }
}
