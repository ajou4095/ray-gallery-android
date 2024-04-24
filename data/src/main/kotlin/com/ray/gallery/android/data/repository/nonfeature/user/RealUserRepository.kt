package com.ray.gallery.android.data.repository.nonfeature.user

import com.ray.gallery.android.data.remote.network.api.nonfeature.UserApi
import com.ray.gallery.android.data.remote.network.util.toDomain
import com.ray.gallery.android.domain.model.nonfeature.user.Profile
import com.ray.gallery.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class RealUserRepository @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getProfile(): Result<Profile> {
        return userApi.getProfile().toDomain()
    }
}
