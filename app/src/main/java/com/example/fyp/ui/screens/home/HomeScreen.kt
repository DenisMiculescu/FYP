package com.example.fyp.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fyp.navigation.*
import com.example.fyp.ui.components.general.BottomAppBarProvider
import com.example.fyp.ui.components.general.TopAppBarProvider
import com.example.fyp.ui.components.receipt.ReceiptUploadDialog
import com.example.fyp.ui.screens.map.MapViewModel
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

    val currentUser = homeViewModel.currentUser
    val isActiveSession = homeViewModel.isAuthenticated()
    val userEmail = if (isActiveSession) currentUser?.email else ""
    val userName = if (isActiveSession) currentUser?.displayName else ""

    val userDestinations = if (!isActiveSession)
        userSignedOutDestinations
    else bottomAppBarDestinations

    val startScreen = remember(isActiveSession) {
        if (isActiveSession) Report else Login
    }

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    var showReceiptDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isActiveSession) {
        if (isActiveSession && locationPermissions.allPermissionsGranted) {
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
                navController = navController,
                currentScreen = currentBottomScreen,
                destinations = userDestinations,
                onAddReceiptClick = { showReceiptDialog = true }
            )
        }
    )

    ReceiptUploadDialog(
        showDialog = showReceiptDialog,
        onDismiss = { showReceiptDialog = false },
        onUploadComplete = {
            showReceiptDialog = false
            navController.navigate(Report.route)
        }
    )
}
