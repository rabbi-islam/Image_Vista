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
import com.example.imagevista.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val downloader: Downloader,
    saveStateHandle: SavedStateHandle
) : ViewModel() {

    private val imageId = saveStateHandle.toRoute<Routes.FullImageScreen>().imageId

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()


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
            } catch (e: UnknownHostException) {
                _snackBarEvent.send(SnackBarEvent(message = "No Internet Connection."))
            }
            catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }

        }

    }

    fun downloadImage(url: String, fileName: String?) {

        viewModelScope.launch {
            try {
                downloader.downloadFile(url, fileName)
            } catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }
        }
    }





}