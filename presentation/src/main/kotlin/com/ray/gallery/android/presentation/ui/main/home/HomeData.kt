package com.ray.gallery.android.presentation.ui.main.home

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import com.ray.gallery.android.presentation.model.FolderModel
import com.ray.gallery.android.presentation.model.ImageModel

@Immutable
data class HomeData(
    val folderList: List<FolderModel>,
    val imageModelList: LazyPagingItems<ImageModel>
)
