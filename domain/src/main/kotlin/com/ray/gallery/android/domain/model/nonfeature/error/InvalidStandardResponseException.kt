package com.ray.gallery.android.domain.model.nonfeature.error

class InvalidStandardResponseException(
    override val message: String
) : Exception(message)
