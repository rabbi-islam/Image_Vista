package com.example.imagevista.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imagevista.data.local.entity.FavouriteImageEntity
import com.example.imagevista.data.local.entity.UnsplashImageEntity
import com.example.imagevista.data.local.entity.UnsplashRemoteKeys

@Database(
    entities = [FavouriteImageEntity::class, UnsplashImageEntity::class, UnsplashRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ImageVistaDatabase: RoomDatabase() {

    abstract fun favouriteImagesDao(): FavoriteImagesDao
    abstract fun editorialFeedDao(): EditorialFeedDao

}