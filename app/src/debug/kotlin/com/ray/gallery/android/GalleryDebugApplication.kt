package com.ray.gallery.android

import timber.log.Timber

class GalleryDebugApplication : GalleryApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
