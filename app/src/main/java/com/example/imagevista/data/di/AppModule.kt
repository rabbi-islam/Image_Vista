package com.example.imagevista.data.di

import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.repository.ImageRepositoryImpl
import com.example.imagevista.data.util.Constants.BASE_URL
import com.example.imagevista.domain.repository.ImageRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideUnsplashApiService(): UnsplashApiService {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
        return retrofit.create(UnsplashApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageRepository(apiService: UnsplashApiService):ImageRepository{
        return ImageRepositoryImpl(apiService)
    }


}