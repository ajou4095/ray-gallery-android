package com.ray.gallery.android.presentation.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.common.util.coroutine.event.eventObserve
import com.ray.gallery.android.presentation.R
import com.ray.gallery.android.presentation.common.theme.GalleryTheme
import com.ray.gallery.android.presentation.common.util.compose.ErrorObserver
import com.ray.gallery.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import com.ray.gallery.android.presentation.common.util.compose.safeNavigate
import com.ray.gallery.android.presentation.common.view.DialogScreen
import com.ray.gallery.android.presentation.ui.main.home.homeDestination
import com.ray.gallery.android.presentation.ui.main.nonlogin.nonLoginNavGraphNavGraph
import com.ray.gallery.android.presentation.ui.main.splash.SplashConstant
import com.ray.gallery.android.presentation.ui.main.splash.splashDestination

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    GalleryTheme {
        val navController = rememberNavController()

        ErrorObserver(viewModel)
        MainScreenRefreshFailDialog(navController, viewModel.refreshFailEvent)

        NavHost(
            navController = navController,
            startDestination = SplashConstant.ROUTE
        ) {
            splashDestination(navController = navController)
            nonLoginNavGraphNavGraph(navController = navController)
            homeDestination(navController = navController)
        }
    }
}

@Composable
fun MainScreenRefreshFailDialog(
    navController: NavHostController,
    refreshFailEvent: EventFlow<Unit>
) {
    var isInvalidTokenDialogShowing: Boolean by remember { mutableStateOf(false) }

    if (isInvalidTokenDialogShowing) {
        DialogScreen(
            isCancelable = false,
            title = stringResource(R.string.invalid_jwt_token_dialog_title),
            message = stringResource(R.string.invalid_jwt_token_dialog_content),
            onConfirm = {
                navController.safeNavigate(SplashConstant.ROUTE)
            },
            onDismissRequest = {
                isInvalidTokenDialogShowing = false
            }
        )
    }

    LaunchedEffectWithLifecycle(refreshFailEvent) {
        refreshFailEvent.eventObserve {
            isInvalidTokenDialogShowing = true
        }
    }
}
