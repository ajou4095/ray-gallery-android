package com.ray.gallery.android.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderModel(
    val name: String,
    val location: String?,
) : Parcelable {
    companion object {
        val recent = FolderModel("최근 항목", "")
    }
}
