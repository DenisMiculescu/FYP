package com.example.fyp.ui.components.report

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fyp.data.ReceiptModel


@Composable
internal fun ReceiptCardList(
    receipts: List<ReceiptModel>,
    modifier: Modifier = Modifier,
    onDeleteReceipt: (ReceiptModel) -> Unit,
    onClickReceiptDetails: (Long) -> Unit,
    onRefreshList: () -> Unit
) {
    LazyColumn {
        items(
            items = receipts,
            key = { receipt -> receipt.id }
        ) { receipt ->
            ReceiptCard(
                receipt = receipt,
                onClickDelete = { onDeleteReceipt(receipt) },
                onClickReceiptDetails = { onClickReceiptDetails(receipt.id) },
                onRefreshList = onRefreshList
            )
        }
    }
}
