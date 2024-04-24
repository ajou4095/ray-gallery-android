package com.ray.gallery.android.data.remote.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ray.gallery.android.data.remote.local.database.sample.SampleDao
import com.ray.gallery.android.data.remote.local.database.sample.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = false)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao

    companion object {
        const val DATABASE_NAME = "gallery"
    }
}
