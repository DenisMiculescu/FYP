package com.example.fyp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.fyp.ui.screens.home.HomeScreen
import com.example.fyp.ui.theme.FYPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FYPMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FYPTheme { HomeScreen() }
        }
    }
}
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FYPApp(
//        modifier: Modifier = Modifier,
//        navController: NavHostController = rememberNavController()
//    ) {
//    val receipts = remember { mutableStateListOf<ReceiptModel>() }
//    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.Receipt) }
//
//    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = currentNavBackStackEntry?.destination
//    val currentBottomScreen =
//        allDestinations.find { it.route == currentDestination?.route } ?: Report
//
//    Scaffold(
//        modifier = modifier,
//        topBar = {
//            TopAppBarProvider(
//                currentScreen = currentBottomScreen,
//                canNavigateBack = navController.previousBackStackEntry != null
//            ) { navController.navigateUp() }
//        },
//        content = { paddingValues ->
//            NavHostProvider(
//                modifier = modifier,
//                navController = navController,
//                paddingValues = paddingValues,
//                receipts = receipts)
//        },
//        bottomBar = {
//            BottomAppBarProvider(navController,
//                currentScreen = currentBottomScreen,)
//        }
//    )
//}
