package com.example.imagevista.presentation.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imagevista.data.di.AppModule
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.dto.UnsplashImageDto
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ImageRepository
):ViewModel() {


    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()


    val images: StateFlow<PagingData<UnsplashImage>> = repository.getEditorialFeedImages()
        .catch {
            _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${it.message}"))
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )


    val favoriteImageIds: StateFlow<List<String>> = repository.getFavoriteImageIds()
        .catch {
            _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${it.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleFavouriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                repository.toggleFavouriteStatus(image)
            } catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent(message = "Something Went Wrong: ${e.message}"))
            }
        }
    }




}