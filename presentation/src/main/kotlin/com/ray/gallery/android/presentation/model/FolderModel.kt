package com.ray.gallery.android.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderModel(
    val filePath: String,
    val name: String,
    val imageList: List<ImageModel>
) : Parcelable
