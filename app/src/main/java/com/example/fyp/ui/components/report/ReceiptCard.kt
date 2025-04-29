package com.example.fyp.ui.components.report

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fyp.R
import com.example.fyp.data.ReceiptModel
import timber.log.Timber
import java.text.DateFormat

@Composable
fun ReceiptCard(
    merchant: String,
    amount: Float,
    dateCreated: String,
    dateModified: String,
    description: String,
    onClickDelete: () -> Unit,
    onClickReceiptDetails: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        ReceiptCardContent(
            merchant,
            amount,
            dateCreated,
            dateModified,
            description,
            onClickDelete,
            onClickReceiptDetails,
//            onRefreshList
        )
    }
}

@Composable
private fun ReceiptCardContent(
    merchant: String,
    amount: Float,
    dateCreated: String,
    dateModified: String,
    description: String,
    onClickDelete: () -> Unit,
    onClickReceiptDetails: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Business,
                    contentDescription = "Merchant Icon",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = merchant,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "€$amount",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }

            Text(
                text = "Date Added: $dateCreated",
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "Date Modified: $dateModified",
                style = MaterialTheme.typography.labelSmall
            )

            if (expanded) {
                Text(
                    modifier = Modifier.padding(vertical = 16.dp),
                    text = description
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FilledTonalButton(onClick = onClickReceiptDetails) {
                        Text(text = "Show More...")
                    }

                    FilledTonalIconButton(onClick = {
                        showDeleteConfirmDialog = true
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete Receipt")
                    }

                    if (showDeleteConfirmDialog) {
                        ShowDeleteAlert(
                            onDismiss = { showDeleteConfirmDialog = false },
                            onDelete = onClickDelete,
//                            onRefresh = onRefreshList
                        )
                    }
                }
            }
        }

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Composable
fun ShowDeleteAlert(
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.confirm_delete)) },
        text = { Text(stringResource(id = R.string.confirm_delete_message)) },
        confirmButton = {
            Button(
                onClick = {
                    onDelete()
//                    onRefresh()
                }
            ) { Text("Yes") }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("No") }
        }
    )
}
