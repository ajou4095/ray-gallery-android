package com.ray.gallery.android.presentation.ui.main

import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.domain.usecase.nonfeature.authentication.token.GetTokenRefreshFailEventFlowUseCase
import com.ray.gallery.android.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTokenRefreshFailEventFlowUseCase: GetTokenRefreshFailEventFlowUseCase
) : BaseViewModel() {

    val refreshFailEvent: EventFlow<Unit> = getTokenRefreshFailEventFlowUseCase()

}
