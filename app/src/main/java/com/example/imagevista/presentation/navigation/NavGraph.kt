package com.example.imagevista.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imagevista.presentation.favourite_screen.FavouriteScreen
import com.example.imagevista.presentation.favourite_screen.FavouriteViewModel
import com.example.imagevista.presentation.full_image_screen.FullImageScreen
import com.example.imagevista.presentation.full_image_screen.FullImageViewModel
import com.example.imagevista.presentation.home_screen.HomeScreen
import com.example.imagevista.presentation.home_screen.HomeViewModel
import com.example.imagevista.presentation.profile_screen.ProfileScreen
import com.example.imagevista.presentation.search_screen.SearchScreen
import com.example.imagevista.presentation.search_screen.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val viewmodel: HomeViewModel = hiltViewModel()
            HomeScreen(
                snackbarHostState = snackBarHostState,
                snackBarEvent = viewmodel.snackBarEvent,
                scrollBehavior = scrollBehavior,
                images = viewmodel.images,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                onSearchClick = {
                    navController.navigate(Routes.SearchScreen)
                },
                onFavClick = {
                    navController.navigate(Routes.FavouritesScreen)
                }
            )
        }

        composable<Routes.SearchScreen> {
            val searchViewModel: SearchViewModel = hiltViewModel()
            val searchedImages = searchViewModel.searchImages.collectAsLazyPagingItems()
            val favoriteImageIds by searchViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            SearchScreen(
                snackbarHostState = snackBarHostState,
                snackBarEvent = searchViewModel.snackBarEvent,
                onBackArrowClick = { navController.navigateUp() },
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                searchedImage = searchedImages,
                onSearch = { searchViewModel.searchImages(it) },
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onToggleFavouriteStatus = { searchViewModel.toggleFavouriteStatus(it) },
                favoriteImageIds = favoriteImageIds
            )
        }

        composable<Routes.FavouritesScreen> {
            val favouriteViewModel: FavouriteViewModel = hiltViewModel()
            val favouriteImages = favouriteViewModel.favoriteImages.collectAsLazyPagingItems()
            val favoriteImageIds by favouriteViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            FavouriteScreen(
                snackbarHostState = snackBarHostState,
                favoriteImages = favouriteImages,
                favoriteImageIds = favoriteImageIds,
                snackBarEvent = favouriteViewModel.snackBarEvent,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId))
                },
                onBackArrowClick = { navController.navigateUp() },
                onToggleFavouriteStatus = { favouriteViewModel.toggleFavouriteStatus(it) },
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                scrollBehavior = scrollBehavior
            )
        }

        composable<Routes.FullImageScreen> {
            val fullImageViewModel: FullImageViewModel = hiltViewModel()
            FullImageScreen(
                snackbarHostState = snackBarHostState,
                snackBarEvent = fullImageViewModel.snackBarEvent,
                image = fullImageViewModel.image,
                onBackArrowClick = { navController.navigateUp() },
                onPhotographerImageClick = { profileLink ->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownloadClick = { url, title ->
                    fullImageViewModel.downloadImage(url, title)
                }

            )
        }

        composable<Routes.ProfileScreen> { backStackEntry ->
            val profileLink = backStackEntry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}