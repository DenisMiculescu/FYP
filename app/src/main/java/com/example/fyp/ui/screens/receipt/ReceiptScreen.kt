package com.example.fyp.ui.screens.receipt

import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fyp.ui.components.receipt.*
import timber.log.Timber
import java.io.File

@Composable
fun ReceiptScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    receiptViewModel: ReceiptViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var merchant by remember { mutableStateOf("") }
    var amount by remember { mutableIntStateOf(10) }
    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val photoFile = remember {
        File(
            context.cacheDir,
            "receipt_${System.currentTimeMillis()}.jpg"
        )
    }
    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoFile.exists()) {
            receiptViewModel.uploadReceiptImage(photoUri, merchant, amount.toFloat(), description, email)
            navController.navigate("report")
        } else {
            Timber.e("Camera capture failed or file not found at ${photoFile.path}")
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (granted) {
            launcherCamera.launch(photoUri)
        }
    }

    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            receiptViewModel.uploadReceiptImage(it, merchant, amount.toFloat(), description, email)
            navController.navigate("report")
        }
    }

    Column {
        Column(
            modifier = modifier.padding(start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            WelcomeText()
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier.weight(1f))
                AmountPicker(onPaymentAmountChange = { amount = it })
            }
            MerchantInput(modifier = modifier, onMessageChange = { merchant = it })
            DescriptionInput(modifier = modifier, onMessageChange = { description = it })

            Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "+ Add Receipt")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Upload Receipt") },
            text = { Text("Choose an option") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    if (hasCameraPermission) {
                        launcherCamera.launch(photoUri)
                    } else {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Take Photo")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    launcherGallery.launch("image/*")
                }) {
                    Text("Upload Image")
                }
            }
        )
    }
}
