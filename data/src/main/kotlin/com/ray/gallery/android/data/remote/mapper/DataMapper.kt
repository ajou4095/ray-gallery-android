package com.ray.gallery.android.data.remote.mapper

interface DataMapper<D> {
    fun toDomain(): D
}
