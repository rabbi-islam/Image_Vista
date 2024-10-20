package com.example.imagevista.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.imagevista.data.local.ImageVistaDatabase
import com.example.imagevista.data.mapper.toDomainModel
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.mapper.toFavoriteImageEntity
import com.example.imagevista.data.paging.EditorialFeedRemoteMediator
import com.example.imagevista.data.paging.SearchPagingSource
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.util.Constants
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class ImageRepositoryImpl(
    private val unsplashApi: UnsplashApiService,
    private val database: ImageVistaDatabase,
) : ImageRepository {

    private val favouriteImagesDao = database.favouriteImagesDao()
    private val editorialImagesDao = database.editorialFeedDao()

    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEM_PER_PAGE),
            remoteMediator = EditorialFeedRemoteMediator(unsplashApi, database),
            pagingSourceFactory = {
                editorialImagesDao.getAllEditorialFeedImages()
            }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }

    override suspend fun getImage(imageId: String): UnsplashImage {
        return unsplashApi.getImage(imageId).toDomainModel()
    }

    override fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {

        return Pager(
            config = PagingConfig(pageSize = Constants.ITEM_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(query, unsplashApi)
            }
        ).flow

    }

    override suspend fun toggleFavouriteStatus(image: UnsplashImage) {

        val isFavourite = favouriteImagesDao.isImageFavorite(image.id)
        val favouriteImage = image.toFavoriteImageEntity()
        if (isFavourite) {
            favouriteImagesDao.deleteFavoriteImage(favouriteImage)
        } else {
            favouriteImagesDao.insertFavoriteImage(favouriteImage)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> {
        return favouriteImagesDao.getFavoriteImageIds()
    }

    override fun getAllFavouriteImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEM_PER_PAGE),
            pagingSourceFactory = { favouriteImagesDao.getAllFavoriteImages() }
        )
            .flow
            .map { pagingData ->
                pagingData.map { it.toDomainModel() }
            }
    }
}