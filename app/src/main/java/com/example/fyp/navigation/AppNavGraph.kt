package com.example.fyp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fyp.ui.screens.map.MapScreen
import com.example.fyp.ui.screens.about.AboutScreen
import com.example.fyp.ui.screens.details.DetailsScreen
import com.example.fyp.ui.screens.home.HomeScreen
import com.example.fyp.ui.screens.login.LoginScreen
import com.example.fyp.ui.screens.profile.ProfileScreen
import com.example.fyp.ui.screens.register.RegisterScreen
import com.example.fyp.ui.screens.report.ReportScreen


@Composable
fun NavHostProvider(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: AppDestination,
    paddingValues: PaddingValues,
    permissions: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = Modifier.padding(paddingValues = paddingValues)) {

        composable(route = Home.route) {
            HomeScreen(modifier = modifier)
        }

        composable(route = Report.route) {
            ReportScreen(modifier = modifier,
                onClickReceiptDetails = {
                    receiptId : String ->
                    navController.navigateToReceiptDetails(receiptId)
                },
            )
        }

        composable(route = About.route) {
            AboutScreen(modifier = modifier)
        }

        composable(
            route = Details.route,
            arguments = Details.arguments
        )
        { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString(Details.idArg)
            if (id != null) {
                DetailsScreen()
            }
        }

        composable(route = Login.route) {
            LoginScreen(
                navController = navController,
                onLogin = { navController.popBackStack() }
            )
        }

        composable(route = Register.route) {
            RegisterScreen(
                navController = navController,
                onRegister = { navController.popBackStack() }
            )
        }

        composable(route = Profile.route) {
            ProfileScreen(
                onSignOut = {
                    navController.popBackStack()
                    navController.navigate(Login.route) {
                        popUpTo(Home.route) { inclusive = true }
                    }
                },
            )
        }

        composable(route = Map.route) {
            MapScreen(permissions = permissions)
        }

    }
}

private fun NavHostController.navigateToReceiptDetails(receiptId: String) {
    this.navigate("details/$receiptId")
}