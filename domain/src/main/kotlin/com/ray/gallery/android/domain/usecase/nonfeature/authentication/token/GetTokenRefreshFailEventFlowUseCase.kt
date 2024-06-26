package com.ray.gallery.android.domain.usecase.nonfeature.authentication.token

import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.domain.repository.nonfeature.TokenRepository
import javax.inject.Inject

class GetTokenRefreshFailEventFlowUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): EventFlow<Unit> {
        return tokenRepository.refreshFailEvent
    }
}
