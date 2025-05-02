package com.example.fyp.ui.components.general

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.fyp.navigation.AddReceipt
import com.example.fyp.navigation.AppDestination

@Composable
fun BottomAppBarProvider(
    navController: NavController,
    currentScreen: AppDestination,
    destinations: List<AppDestination>,
    onAddReceiptClick: () -> Unit
) {
    NavigationBar {
        destinations.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentScreen.route == screen.route,
                onClick = {
                    if (screen == AddReceipt) {
                        onAddReceiptClick()
                    } else {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                        }
                    }
                }
            )
        }
    }
}
