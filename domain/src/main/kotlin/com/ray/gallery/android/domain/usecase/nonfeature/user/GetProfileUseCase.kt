package com.ray.gallery.android.domain.usecase.nonfeature.user

import com.ray.gallery.android.domain.model.nonfeature.user.Profile
import com.ray.gallery.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Profile> {
        return userRepository.getProfile()
    }
}
