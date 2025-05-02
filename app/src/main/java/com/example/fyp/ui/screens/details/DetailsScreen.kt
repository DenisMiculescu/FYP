package com.example.fyp.ui.screens.details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.fyp.ui.components.details.DetailsScreenText
import com.example.fyp.ui.components.details.ReadOnlyTextField
import com.example.fyp.ui.components.general.ShowLoader
import timber.log.Timber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailsViewModel = hiltViewModel()
) {
    val receipt = detailViewModel.receipt.value
    val context = LocalContext.current
    val isError = detailViewModel.isErr.value
    val error = detailViewModel.error.value
    val isLoading = detailViewModel.isLoading.value

    if (isLoading) {
        ShowLoader("Retrieving receipt details...")
        return
    }

    if (isError) {
        Toast.makeText(context, "Unable to fetch receipt at this time", Toast.LENGTH_SHORT).show()
        Timber.e("DetailsScreen Error: $error")
        return
    }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DetailsScreenText()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            if (receipt.receiptImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(receipt.receiptImageUrl),
                    contentDescription = "Receipt Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(bottom = 12.dp)
                )
            }

            ReadOnlyTextField(value = receipt.merchant, label = "Merchant")
            ReadOnlyTextField(value = "€%.2f".format(receipt.amount), label = "Total Amount")
            ReadOnlyTextField(value = receipt.dateCreated.toString(), label = "Date Created")

            if (receipt.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Items Purchased",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                receipt.items.forEach { item ->
                    Text(text = "• $item", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No item details available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
