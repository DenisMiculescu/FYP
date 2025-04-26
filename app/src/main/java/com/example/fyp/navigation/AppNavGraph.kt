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
import com.example.fyp.ui.screens.ScreenReceipt
import com.example.fyp.ui.screens.ScreenReport


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
            ScreenReceipt(modifier = modifier, receipts = receipts)
        }
        composable(route = Report.route) {
            ScreenReport(modifier = modifier, receipts = receipts)
        }
    }
}