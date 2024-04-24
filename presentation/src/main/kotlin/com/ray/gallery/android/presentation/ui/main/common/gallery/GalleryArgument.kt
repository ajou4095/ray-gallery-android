package com.ray.gallery.android.presentation.ui.main.common.gallery

import androidx.compose.runtime.Immutable
import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.presentation.model.gallery.GalleryFolder
import kotlinx.coroutines.CoroutineExceptionHandler

@Immutable
data class GalleryArgument(
    val state: GalleryState,
    val event: EventFlow<GalleryEvent>,
    val intent: (GalleryIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val handler: CoroutineExceptionHandler
)

sealed interface GalleryState {
    data object Init : GalleryState
}

sealed interface GalleryEvent

sealed interface GalleryIntent {
    data object OnGrantPermission : GalleryIntent
    data class OnChangeFolder(
        val folder: GalleryFolder
    ) : GalleryIntent
}
