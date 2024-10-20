package com.example.imagevista.data.util

import com.example.imagevista.BuildConfig

object Constants {
    const val API_KEY = BuildConfig.UNSPLASH_API_KEY
    const val BASE_URL = "https://api.unsplash.com/"
    const val ITEM_PER_PAGE = 10
    const val FAVOURITE_IMAGES_TABLE = "favourite_images_table"
    const val UNSPLASH_IMAGES_TABLE = "images_table"
    const val IMAGE_VISTA_DATABASE = "unsplash_images.db"
    const val REMOTE_KEYS_TABLE = "remote_keys_table"

}