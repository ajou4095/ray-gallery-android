package com.ray.gallery.android.data.remote.local.di

import android.content.Context
import androidx.room.Room
import com.ray.gallery.android.data.remote.local.SharedPreferencesManager
import com.ray.gallery.android.data.remote.local.database.GalleryDatabase
import com.ray.gallery.android.data.remote.local.database.sample.SampleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesManager(
        @ApplicationContext context: Context
    ): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideGalleryDatabase(
        @ApplicationContext context: Context
    ): GalleryDatabase {
        return Room.databaseBuilder(
            context,
            GalleryDatabase::class.java,
            GalleryDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSampleDao(
        galleryDatabase: GalleryDatabase
    ): SampleDao {
        return galleryDatabase.sampleDao()
    }
}
