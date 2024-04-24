package com.ray.gallery.android.domain.repository.nonfeature

import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.domain.model.nonfeature.authentication.JwtToken

interface TokenRepository {

    var refreshToken: String

    var accessToken: String

    val refreshFailEvent: EventFlow<Unit>

    suspend fun refreshToken(
        refreshToken: String
    ): Result<JwtToken>
}
