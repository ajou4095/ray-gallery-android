package com.ray.gallery.android.presentation.ui.main.home

import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import com.ray.gallery.android.common.util.coroutine.event.EventFlow
import com.ray.gallery.android.common.util.coroutine.event.MutableEventFlow
import com.ray.gallery.android.common.util.coroutine.event.asEventFlow
import com.ray.gallery.android.presentation.common.base.BaseViewModel
import com.ray.gallery.android.presentation.model.FolderModel
import com.ray.gallery.android.presentation.model.ImageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
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
                launch {
                    asdf()
                }
            }
        }
    }

    suspend fun asdf() = withContext(Dispatchers.IO) {
//        val folderList: MutableMap<String, MutableList<ImageModel>> = mutableMapOf()

        val fileExtensionList = listOf("png", "jpg", "jpeg", "gif")

        suspend fun dfs(parent: File) {
            parent.listFiles()?.forEach {  child ->
                if (child.isDirectory) {
                    println("asdfasdf ${child.absolutePath} 디렉토리")
                    dfs(child)
                } else {
                    if (fileExtensionList.contains(child.extension.lowercase())) {
                        val parentFilePath = child.parent ?: return
                        val parentFileName = child.parentFile?.name ?: return

                        val image = ImageModel(
                            filePath = child.absolutePath,
                            name = child.name,
                            date = child.lastModified().toString()
                        )


                        folderList.value.indexOfFirst { it.filePath == parentFilePath }
                            .takeIf { it != -1 }
                            ?.let { index ->
                                println("asdfasdf ${parentFilePath} 이미있음")
                                _folderList.value = ArrayList(folderList.value).apply {
                                    this[index] = folderList.value[index].copy(
                                        imageList = folderList.value[index].imageList + image
                                    )
                                }
                            } ?: let {
                            _folderList.value = ArrayList(folderList.value).apply {
                                println("asdfasdf ${parentFilePath} 추가")
                                add(
                                    FolderModel(
                                        filePath = parentFilePath,
                                        name = parentFileName,
                                        imageList = listOf(image)
                                    )
                                )
                            }
                        }
                    } else {
                        println("asdfasdf ${child.absolutePath} 확장자 없음")
                    }
                }
            }
        }

        dfs(Environment.getExternalStorageDirectory())
    }
}
