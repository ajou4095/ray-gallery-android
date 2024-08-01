package com.ray.gallery.android.presentation.ui.main.home

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.ray.gallery.android.presentation.model.FolderModel
import com.ray.gallery.android.presentation.model.ImageModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class HomeCursor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val collection: Uri by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
    }

    private val projection: Array<String> = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATE_TAKEN
    )

    fun getFolderList(): List<FolderModel> {
        val folderList: MutableList<FolderModel> = mutableListOf()

        getCursor()?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)
                    .takeIf { it != -1 }
                    ?.let { cursor.getLong(it) }
                    ?: continue

                val name = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                    .takeIf { it != -1 }
                    ?.let { cursor.getString(it) }
                    ?: continue

                val filePath = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    .takeIf { it != -1 }
                    ?.let { cursor.getString(it) }
                    ?: continue

                val date = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)
                    .takeIf { it != -1 }
                    ?.let { cursor.getString(it) }
                    ?: continue

                val parentFilePath = File(filePath).parent ?: continue
                val parentFileName = File(filePath).parentFile?.name ?: continue

                val image = ImageModel(
                    id = id,
                    filePath = filePath,
                    name = name,
                    date = date
                )

                folderList.indexOfFirst { it.filePath == parentFilePath }
                    .takeIf { it != -1 }
                    ?.let { index ->
                        folderList[index] = folderList[index].copy(
                            imageList = folderList[index].imageList + image
                        )
                    } ?: let {
                    folderList.add(
                        FolderModel(
                            filePath = parentFilePath,
                            name = parentFileName,
                            imageList = listOf(image)
                        )
                    )
                }
            }
        }

        return folderList
    }

    private fun getCursor(
        offset: Int? = null,
        limit: Int? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): Cursor? {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = bundleOf(
                ContentResolver.QUERY_ARG_OFFSET to offset,
                ContentResolver.QUERY_ARG_LIMIT to limit,
                ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED),
                ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                ContentResolver.QUERY_ARG_SQL_SELECTION to selection,
                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to selectionArgs,
            )
            context.contentResolver.query(
                collection,
                projection,
                bundle,
                null
            )
        } else {
            context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC LIMIT $limit OFFSET $offset",
            )
        }
    }
}
