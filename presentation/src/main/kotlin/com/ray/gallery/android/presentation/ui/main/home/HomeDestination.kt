package com.ray.gallery.android.presentation.ui.main.home

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.ray.gallery.android.presentation.common.util.compose.ErrorObserver

fun NavGraphBuilder.homeDestination(
    navController: NavController
) {
    composable(
        route = HomeConstant.ROUTE
    ) {
        val viewModel: HomeViewModel = hiltViewModel()

        val argument: HomeArgument = Unit.let {
            val state by viewModel.state.collectAsStateWithLifecycle()

            HomeArgument(
                state = state,
                event = viewModel.event,
                intent = viewModel::onIntent,
                logEvent = viewModel::logEvent,
                handler = viewModel.handler
            )
        }

        val data: HomeData = Unit.let {
            val folderList by viewModel.folderList.collectAsStateWithLifecycle()
            val galleryImageList = viewModel.galleryImageList.collectAsLazyPagingItems()

            HomeData(
                folderList = folderList,
                imageModelList = galleryImageList
            )
        }

        ErrorObserver(viewModel)
        HomeScreen(
            navController = navController,
            argument = argument,
            data = data
        )
    }
}
