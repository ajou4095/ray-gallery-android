package com.ray.gallery.android.domain.usecase.nonfeature.authentication

import com.ray.gallery.android.domain.repository.nonfeature.AuthenticationRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authenticationRepository.withdraw()
    }
}
