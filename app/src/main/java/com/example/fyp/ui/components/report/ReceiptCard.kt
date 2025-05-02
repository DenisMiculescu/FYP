package com.example.fyp.ui.components.report

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ReceiptCard(
    merchant: String,
    amount: Float,
    date: String,
    receiptImageUrl: String,
    items: List<String>,
    onClickDelete: () -> Unit,
    onClickReceiptDetails: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 2.dp)
            .fillMaxWidth()
    ) {
        ReceiptCardContent(
            merchant = merchant,
            amount = amount,
            date = date,
            receiptImageUrl = receiptImageUrl,
            items = items,
            onClickDelete = onClickDelete,
            onClickReceiptDetails = onClickReceiptDetails
        )
    }
}

@Composable
private fun ReceiptCardContent(
    merchant: String,
    amount: Float,
    date: String,
    receiptImageUrl: String,
    items: List<String>,
    onClickDelete: () -> Unit,
    onClickReceiptDetails: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = merchant,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "€%.2f".format(amount),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
        }

        Text(text = "Date: $date", style = MaterialTheme.typography.labelSmall)

        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))

            if (items.isNotEmpty()) {
                Text(
                    text = "Items Purchased:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                items.forEach { item ->
                    Text(text = "- $item", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                Text(text = "No item list found.", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            FilledTonalButton(onClick = { showImageDialog = true }) {
                Text("View Receipt Image")
            }

            FilledTonalButton(onClick = onClickReceiptDetails) {
                Text("Show More...")
            }

            FilledTonalIconButton(onClick = { showDeleteConfirmDialog = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Receipt")
            }

            if (showDeleteConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteConfirmDialog = false
                            onClickDelete()
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirmDialog = false }) {
                            Text("No")
                        }
                    },
                    title = { Text("Delete Receipt") },
                    text = { Text("Are you sure you want to delete this receipt?") }
                )
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Show less" else "Show more"
            )
        }

        if (showImageDialog) {
            AlertDialog(
                onDismissRequest = { showImageDialog = false },
                confirmButton = {
                    TextButton(onClick = { showImageDialog = false }) {
                        Text("Close")
                    }
                },
                text = {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(receiptImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Receipt Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f / 6f)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            )
        }
    }
}
