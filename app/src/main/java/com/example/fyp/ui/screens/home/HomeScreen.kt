package com.example.fyp.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fyp.ui.screens.map.MapViewModel
import com.example.fyp.navigation.Login
import com.example.fyp.navigation.NavHostProvider
import com.example.fyp.navigation.Report
import com.example.fyp.navigation.allDestinations
import com.example.fyp.navigation.bottomAppBarDestinations
import com.example.fyp.navigation.userSignedOutDestinations
import com.example.fyp.ui.components.general.BottomAppBarProvider
import com.example.fyp.ui.components.general.TopAppBarProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    mapViewModel: MapViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentNavBackStackEntry?.destination
    val currentBottomScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Login
    var startScreen = currentBottomScreen

    val currentUser = homeViewModel.currentUser
    val isActiveSession = homeViewModel.isAuthenticated()
    val userEmail = if (isActiveSession) currentUser?.email else ""
    val userName = if (isActiveSession) currentUser?.displayName else ""

    val userDestinations = if (!isActiveSession)
        userSignedOutDestinations
    else bottomAppBarDestinations

    if (isActiveSession) startScreen = Report

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    if (isActiveSession) {
        startScreen = Report
        LaunchedEffect(locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
            if (locationPermissions.allPermissionsGranted) {
                mapViewModel.getLocationUpdates()
            }
        }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBarProvider(
                    navController = navController,
                    currentScreen = currentBottomScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    email = userEmail ?: "",
                    name = userName ?: ""
                ) { navController.navigateUp() }
            },
            content = { paddingValues ->
                NavHostProvider(
                    modifier = modifier,
                    navController = navController,
                    startDestination = startScreen,
                    paddingValues = paddingValues,
                    permissions = locationPermissions.allPermissionsGranted
                )
            },
            bottomBar = {
                BottomAppBarProvider(
                    navController,
                    currentScreen = currentBottomScreen,
                    userDestinations
                )
            }
        )
    }
}
