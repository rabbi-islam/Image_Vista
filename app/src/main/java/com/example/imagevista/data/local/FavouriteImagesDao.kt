package com.example.imagevista.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.imagevista.data.local.entity.FavouriteImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteImagesDao {

    @Query("SELECT * FROM favourite_images_table")
    fun getAllFavoriteImages(): PagingSource<Int, FavouriteImageEntity>

    @Upsert
    suspend fun insertFavoriteImage(image: FavouriteImageEntity)

    @Delete
    suspend fun deleteFavoriteImage(image: FavouriteImageEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_images_table WHERE id = :id)")
    suspend fun isImageFavorite(id: String): Boolean

    @Query("SELECT id FROM favourite_images_table")
    fun getFavoriteImageIds(): Flow<List<String>>

}