package com.example.fyp.ui.components.report

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fyp.data.models.ReceiptModel
import java.text.DateFormat

@Composable
internal fun ReceiptCardList(
    receipts: List<ReceiptModel>,
    modifier: Modifier = Modifier,
    onDeleteReceipt: (ReceiptModel) -> Unit,
    onClickReceiptDetails: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = receipts,
            key = { receipt -> receipt._id }
        ) { receipt ->
            ReceiptCard(
                merchant = receipt.merchant,
                amount = receipt.amount,
                date = receipt.date,
                receiptImageUrl = receipt.receiptImageUrl,
                items = receipt.items,
                onClickDelete = { onDeleteReceipt(receipt) },
                onClickReceiptDetails = { onClickReceiptDetails(receipt._id) }
            )
        }
    }
}