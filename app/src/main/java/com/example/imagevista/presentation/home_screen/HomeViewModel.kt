package com.example.imagevista.presentation.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagevista.data.di.AppModule
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.dto.UnsplashImageDto
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {
    var images:List<UnsplashImage> by mutableStateOf(emptyList())
        private set

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    init {
        getImages()
    }

    private fun getImages(){

        viewModelScope.launch {
            try {
                val result = repository.getEditorialFeedImages()
                images = result
            } catch (e: UnknownHostException) {
                _snackBarEvent.send(SnackBarEvent(message = "No Internet Connection."))
            }
            catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }

        }
    }

}