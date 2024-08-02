package com.ray.gallery.android.presentation.ui.main.home

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
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

    fun getFolderList(): List<FolderModel> {
        val folderList: MutableList<FolderModel> = mutableListOf()

        val projection: Array<String> = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        getCursor(projection)?.use { cursor ->
            while (cursor.moveToNext()) {
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

    fun getPrivateFolderList(): List<FolderModel> {
        val folderList: MutableList<FolderModel> = mutableListOf()

        val fileExtensionList = listOf("png", "jpg", "jpeg", "gif")

        fun dfs(parent: File) {
            parent.listFiles()?.forEach {  child ->
                if (child.isDirectory) {
                    dfs(child)
                } else {
                    if (fileExtensionList.contains(child.extension)) {
                        val parentFilePath = child.parent ?: return
                        val parentFileName = child.parentFile?.name ?: return

                        val image = ImageModel(
                            filePath = child.absolutePath,
                            name = child.name,
                            date = child.lastModified().toString()
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
                }
        }

            dfs(Environment.getExternalStorageDirectory())

        return folderList


//
//        val projection = arrayOf(
//            MediaStore.Files.FileColumns.DATA,
////            MediaStore.Files.FileColumns.MEDIA_TYPE,
//            MediaStore.Files.FileColumns.DISPLAY_NAME
//        )
////        "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_NONE} AND " +
//
////        val where = "${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE ?"
////
////        val params = arrayOf<String>("%${MediaStore.MEDIA_IGNORE_FILENAME}%")
//
//        val where = null
//        val params = null
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//            val bundle = bundleOf(
//                ContentResolver.QUERY_ARG_SQL_SELECTION to where,
//                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS to params,
//            )
//            context.contentResolver.query(
//                collection,
//                projection,
//                bundle,
//                null
//            )
//        } else {
//            context.contentResolver.query(
//                collection,
//                projection,
//                where,
//                params,
//                null
//            )
//        }?.use { cursor ->
//            println("asdfasdf ${cursor.count}")
//            while (cursor.moveToNext()) {
//                println("asdfasdf : search private folder")

//            }
//        }
//
//        return folderList
    }

    private fun getCursor(
        projection: Array<String>,
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
