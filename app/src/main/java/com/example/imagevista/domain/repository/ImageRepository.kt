package com.example.imagevista.domain.repository

import androidx.paging.PagingData
import com.example.imagevista.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    suspend fun getImage(imageId:String):UnsplashImage
    fun searchImages(query:String):Flow<PagingData<UnsplashImage>>
    fun getAllFavouriteImages():Flow<PagingData<UnsplashImage>>
    suspend fun toggleFavouriteStatus(image: UnsplashImage)
    fun getFavoriteImageIds(): Flow<List<String>>


}