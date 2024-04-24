package com.ray.gallery.android.domain.repository.nonfeature

import com.ray.gallery.android.domain.model.nonfeature.user.Profile

interface UserRepository {

    suspend fun getProfile(): Result<Profile>
}
