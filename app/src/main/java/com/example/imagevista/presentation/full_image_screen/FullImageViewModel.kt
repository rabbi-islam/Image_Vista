package com.example.imagevista.presentation.full_image_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.Downloader
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageId = saveStateHandle.toRoute<Routes.FullImageScreen>().imageId
    var image by mutableStateOf<UnsplashImage?>(null)
        private set

    init {
        getImage()
    }

    private fun getImage() {
        viewModelScope.launch {
            try {
                val result = repository.getImage(imageId)
                image = result
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun downloadImage(url: String, fileName: String?) {

        viewModelScope.launch {
            try {
                downloader.downloadFile(url, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }





}