package com.example.fyp.ui.components.general

import androidx.compose.runtime.Composable
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowPhotoPicker(
    onPhotoUriChanged: (Uri) -> Unit,
) {
    val context = LocalContext.current
    var photoUri: Uri? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            val name = context.packageName
            context.grantUriPermission(name, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            onPhotoUriChanged(uri)
        }
        photoUri = uri
    }

    Column {
        Button(
            onClick = {
                launcher.launch(
                    PickVisualMediaRequest(
                        //Here we request only photos.
                        // Change this to .ImageAndVideo if you want videos too.
                        //Or use .VideoOnly if you only want videos.
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        ) {
            Text("Change Profile Photo")
        }
    }
}
