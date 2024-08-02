package com.ray.gallery.android.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    val filePath: String,
    val name: String,
    val date: String
) : Parcelable
