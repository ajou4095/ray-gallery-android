package com.ray.gallery.android.presentation.ui.main.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ray.gallery.android.presentation.model.ImageModel

class HomePagingSource(
    private val homeCursor: HomeCursor,
    private val currentLocation: String?
) : PagingSource<Int, ImageModel>() {

    override fun getRefreshKey(state: PagingState<Int, ImageModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageModel> {
        return try {
            val position = params.key ?: STARTING_PAGE_IDX
            val data = homeCursor.getPhotoList(
                page = position,
                loadSize = params.loadSize,
                currentLocation = currentLocation
            )
            val endOfPaginationReached = data.isEmpty()
            val prevKey = if (position == STARTING_PAGE_IDX) null else position - 1
            val nextKey =
                if (endOfPaginationReached) null else position + (params.loadSize / PAGING_SIZE)
            LoadResult.Page(data, prevKey, nextKey)

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_IDX = 1
        const val PAGING_SIZE = 30
    }
}