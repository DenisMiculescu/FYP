package com.example.fyp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fyp.data.ReceiptModel
import com.example.fyp.ui.components.general.MenuItem
import com.example.fyp.ui.theme.FYPTheme
import com.example.fyp.ui.theme.screens.ScreenReceipt
import com.example.fyp.ui.theme.screens.ScreenReport

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FYPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FYPApp(modifier = Modifier)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FYPApp(modifier: Modifier = Modifier) {
    val receipts = remember { mutableStateListOf<ReceiptModel>() }
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(MenuItem.Receipt) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if(selectedMenuItem == MenuItem.Receipt) {
                        IconButton(onClick = {
                            selectedMenuItem = MenuItem.Report
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Options",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    else {
                        IconButton(onClick = {
                            selectedMenuItem = MenuItem.Receipt
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Options",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            )
        },
        content = {
            when (selectedMenuItem) {
                MenuItem.Receipt -> ScreenReceipt(modifier = modifier, receipts = receipts)
                MenuItem.Report -> ScreenReport(modifier = modifier, receipts = receipts)
                else -> {}
            }
        }
    )
}
