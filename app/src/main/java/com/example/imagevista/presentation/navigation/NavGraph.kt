package com.example.imagevista.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.imagevista.presentation.favourite_screen.FavouriteScreen
import com.example.imagevista.presentation.full_image_screen.FullImageScreen
import com.example.imagevista.presentation.full_image_screen.FullImageViewModel
import com.example.imagevista.presentation.home_screen.HomeScreen
import com.example.imagevista.presentation.home_screen.HomeViewModel
import com.example.imagevista.presentation.profile_screen.ProfileScreen
import com.example.imagevista.presentation.search_screen.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val viewmodel:HomeViewModel = hiltViewModel()
            HomeScreen(
                scrollBehavior = scrollBehavior,
                images = viewmodel.images,
                onImageClick = {imageId->
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
            SearchScreen(
                onBackArrowClick = {navController.navigateUp()}
            )
        }

        composable<Routes.FavouritesScreen> {
            FavouriteScreen(
                onBackArrowClick = {navController.navigateUp()}
            )
        }

        composable<Routes.FullImageScreen> {
            val fullImageViewModel:FullImageViewModel = hiltViewModel()
            FullImageScreen(
                image = fullImageViewModel.image,
                onBackArrowClick = {navController.navigateUp()},
                onPhotographerImageClick = {profileLink->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownloadClick = {url,title->
                    fullImageViewModel.downloadImage(url,title)
                }

            )
        }

        composable<Routes.ProfileScreen> {backStackEntry->
            val profileLink = backStackEntry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackClick = {navController.navigateUp()}
            )
        }
    }
}