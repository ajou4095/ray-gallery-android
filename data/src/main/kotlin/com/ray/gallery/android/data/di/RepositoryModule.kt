package com.ray.gallery.android.data.di

import com.ray.gallery.android.data.repository.nonfeature.tracking.MockTrackingRepository
import com.ray.gallery.android.domain.repository.nonfeature.TrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsTrackingRepository(
        userRepository: MockTrackingRepository
    ): TrackingRepository
}
