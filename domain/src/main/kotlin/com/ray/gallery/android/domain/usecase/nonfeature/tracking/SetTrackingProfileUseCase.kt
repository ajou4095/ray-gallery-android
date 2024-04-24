package com.ray.gallery.android.domain.usecase.nonfeature.tracking

import com.ray.gallery.android.domain.model.nonfeature.user.Profile
import com.ray.gallery.android.domain.repository.nonfeature.TrackingRepository
import javax.inject.Inject

class SetTrackingProfileUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(
        profile: Profile
    ): Result<Unit> {
        return trackingRepository.setProfile(
            profile = profile
        )
    }
}
