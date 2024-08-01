package com.ray.gallery.android.presentation.ui.main.home

import androidx.lifecycle.SavedStateHandle
import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.common.util.coroutine.event.MutableEventFlow
import com.ray.gallery.android.common.util.coroutine.event.asEventFlow
import com.ray.gallery.android.presentation.common.base.BaseViewModel
import com.ray.gallery.android.presentation.model.FolderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val homeCursor: HomeCursor
) : BaseViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Init)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event: MutableEventFlow<HomeEvent> = MutableEventFlow()
    val event: EventFlow<HomeEvent> = _event.asEventFlow()

    private val _folderList: MutableStateFlow<List<FolderModel>> = MutableStateFlow(listOf())
    val folderList: StateFlow<List<FolderModel>> = _folderList.asStateFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnGrantPermission -> {
                _folderList.value = homeCursor.getFolderList()
            }
        }
    }
}
