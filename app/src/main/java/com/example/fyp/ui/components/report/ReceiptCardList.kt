package com.example.fyp.ui.components.report

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.fyp.data.ReceiptModel
import java.text.DateFormat


@Composable
internal fun ReceiptCardList(
    receipts: SnapshotStateList<ReceiptModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(
            items = receipts,
            key = { receipt -> receipt.id }
        ) { receipt ->
            ReportCard(
                merchant = receipt.merchant,
                amount = receipt.amount,
                description = receipt.description,
                dateCreated = DateFormat.getDateTimeInstance().format(receipt.dateCreated),
            )
        }
    }
}
