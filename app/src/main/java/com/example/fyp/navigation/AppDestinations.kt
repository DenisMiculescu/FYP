package com.example.fyp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.ui.graphics.vector.ImageVector

interface AppDestination {
    val icon: ImageVector
    val label: String
    val route: String
}

object Report : AppDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val label = "Report"
    override val route = "report"
}

object Receipt : AppDestination {
    override val icon = Icons.Filled.AddCircle
    override val label = "Donate"
    override val route = "donate"
}

val bottomAppBarDestinations = listOf(Receipt, Report)
val allDestinations = listOf(Report, Receipt)
