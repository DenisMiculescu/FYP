package com.example.fyp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fyp.data.ReceiptModel
import com.example.fyp.ui.screens.about.AboutScreen
import com.example.fyp.ui.screens.details.DetailsScreen
import com.example.fyp.ui.screens.receipt.ReceiptScreen
import com.example.fyp.ui.screens.report.ReportScreen


@Composable
fun NavHostProvider(
    modifier: Modifier,
    navController: NavHostController,
    paddingValues: PaddingValues,
    receipts: SnapshotStateList<ReceiptModel>
) {
    NavHost(
        navController = navController,
        startDestination = Report.route,
        modifier = Modifier.padding(paddingValues = paddingValues)) {

        composable(route = Receipt.route) {
            ReceiptScreen(modifier = modifier)
        }
        composable(route = Report.route) {
            ReportScreen(modifier = modifier,
                onClickReceiptDetails = {
                        receiptId : Int ->
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
            val id = navBackStackEntry.arguments?.getInt(Details.idArg)
            if (id != null) {
                DetailsScreen()
            }
        }

    }
}

private fun NavHostController.navigateToReceiptDetails(receiptId: Int) {
    this.navigate("details/$receiptId")
}