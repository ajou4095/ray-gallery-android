package com.ray.gallery.android.presentation.ui.main.home

import androidx.compose.runtime.Immutable
import com.ray.gallery.android.presentation.model.FolderModel

@Immutable
data class HomeData(
    val folderList: List<FolderModel>
)
