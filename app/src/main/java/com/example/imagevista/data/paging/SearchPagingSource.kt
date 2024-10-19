package com.example.imagevista.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.domain.model.UnsplashImage

class SearchPagingSource(
    private val query: String,
    private val apiService: UnsplashApiService,
): PagingSource<Int, UnsplashImage>() {

    companion object{
    private const val STARTING_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> {
        val currentPage = params.key ?: 1
       return try {
            val response = apiService.searchImages(
                query = query,
                page = params.key ?: STARTING_PAGE_INDEX,
                perPage = params.loadSize
            )
            val endPaginationReached = response.images.isEmpty()
            LoadResult.Page(
                data = response.images.toDomainModelList(),
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (endPaginationReached) null else currentPage + 1
            )
        }catch (e:Exception){
            return LoadResult.Error(e)
        }
    }
}