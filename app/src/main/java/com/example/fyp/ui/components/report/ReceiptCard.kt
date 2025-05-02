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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
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
            .padding(16.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = merchant,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "€%.2f".format(amount),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))

            Divider()

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Items Purchased",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.primary
            )

            items.forEach { item ->
                Text(
                    text = "• $item",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { showImageDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Image")
                }

                OutlinedButton(
                    onClick = onClickReceiptDetails,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Details")
                }

                IconButton(
                    onClick = { showDeleteConfirmDialog = true },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Receipt",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand"
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
                            .aspectRatio(3f / 4f)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }
            )
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
                title = { Text("Delete Receipt?") },
                text = { Text("Are you sure you want to delete this receipt?") }
            )
        }
    }
}
