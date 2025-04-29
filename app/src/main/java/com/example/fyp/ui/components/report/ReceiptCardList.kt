package com.example.fyp.ui.components.report

import android.net.Uri
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.fyp.data.ReceiptModel
import java.text.DateFormat


@Composable
internal fun ReceiptCardList(
    receipts: List<ReceiptModel>,
    modifier: Modifier = Modifier,
    onDeleteReceipt: (ReceiptModel) -> Unit,
    onClickReceiptDetails: (String) -> Unit,
//    onRefreshList: () -> Unit
) {
    LazyColumn {
        items(
            items = receipts,
            key = { receipt -> receipt._id }
        ) { receipt ->
            ReceiptCard(
                merchant = receipt.merchant,
                amount = receipt.amount,
                dateCreated = DateFormat.getDateTimeInstance().format(receipt.dateCreated),
                dateModified = DateFormat.getDateTimeInstance().format(receipt.dateModified),
                description = receipt.description,
                photoUri = Uri.parse(receipt.imageUri),
                onClickDelete = { onDeleteReceipt(receipt) },
                onClickReceiptDetails = { onClickReceiptDetails(receipt._id) },
//                onRefreshList = onRefreshList
            )
        }
    }
}
