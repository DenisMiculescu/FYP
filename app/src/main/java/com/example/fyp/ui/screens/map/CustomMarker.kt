package com.example.fyp.ui.screens.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun CustomMarker() {
    Box(
        modifier = Modifier
            .height(34.dp)
            .width(30.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .allowHardware(false)
                .size(Size.ORIGINAL)
                .build()
        )

        if (painter.state is AsyncImagePainter.State.Success) {
            Image(
                painter = painter,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape),
                contentDescription = "Pharmacy"
            )
        }
    }
}


