package com.example.fyp.ui.components.receipt

import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.ui.screens.receipt.ReceiptViewModel
import java.io.File

@Composable
fun ReceiptUploadDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onUploadComplete: () -> Unit,
    receiptViewModel: ReceiptViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val photoFile = remember {
        File(context.cacheDir, "receipt_${System.currentTimeMillis()}.jpg")
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    val performOCRAndUpload = { uri: Uri ->
        receiptViewModel.uploadReceiptFromOCR(context, uri)
        onUploadComplete()
    }

    val launcherCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoFile.exists()) {
            performOCRAndUpload(photoUri)
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
        uri?.let { performOCRAndUpload(it) }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Upload Receipt") },
            text = { Text("Choose an option") },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
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
                    onDismiss()
                    launcherGallery.launch("image/*")
                }) {
                    Text("Upload Image")
                }
            }
        )
    }
}
