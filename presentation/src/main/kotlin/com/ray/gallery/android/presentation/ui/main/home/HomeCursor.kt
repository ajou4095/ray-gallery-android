package com.ray.gallery.android.presentation.ui.main.home

import android.annotation.SuppressLint
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

    private val uriExternal: Uri by lazy {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    }

    private val projection = arrayOf(
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.Images.ImageColumns._ID
    )

    private val contentResolver by lazy { context.contentResolver }
    private val sortedOrder = MediaStore.Images.ImageColumns.DATE_TAKEN

    fun getPhotoList(
        page: Int,
        loadSize: Int,
        currentLocation: String?
    ): List<ImageModel> {
        val imageModelList = mutableListOf<ImageModel>()

        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (currentLocation != null) {
            selection = "${MediaStore.Images.Media.DATA} LIKE ?"
            selectionArgs = arrayOf("%$currentLocation%")
        }

        val offset = (page - 1) * loadSize
        val query = getQuery(offset, loadSize, selection, selectionArgs)

        query?.use { cursor ->
            while (cursor.moveToNext()) {
                val id =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME))
                val filePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA))
                val date =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN))
                val image = ImageModel(
                    id = id,
                    filePath = filePath,
                    name = name,
                    date = date ?: ""
                )
                imageModelList.add(image)
            }
        }
        return imageModelList
    }

    fun getFolderList(): List<FolderModel> {
        val folderList: ArrayList<FolderModel> = arrayListOf(FolderModel("최근 항목", ""))
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                File(cursor.getString(columnIndex)).parent?.let { filePath ->
                    val folder = FolderModel(filePath.split("/").last(), filePath)
                    folderList.find {
                        it.location == filePath
                    } ?: folderList.add(folder)
                }
            }
            cursor.close()
        }
        return folderList
    }

    @SuppressLint("Recycle")
    private fun getQuery(
        offset: Int,
        limit: Int,
        selection: String?,
        selectionArgs: Array<String>?,
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
            contentResolver.query(uriExternal, projection, bundle, null)
        } else {
            contentResolver.query(
                uriExternal,
                projection,
                selection,
                selectionArgs,
                "$sortedOrder DESC LIMIT $limit OFFSET $offset",
            )
        }
    }
}
