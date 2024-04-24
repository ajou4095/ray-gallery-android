package com.ray.gallery.android.presentation.ui.main.home.mypage

import androidx.compose.runtime.Immutable
import com.ray.gallery.android.domain.model.nonfeature.user.Profile

@Immutable
data class MyPageData(
    val profile: Profile
)
