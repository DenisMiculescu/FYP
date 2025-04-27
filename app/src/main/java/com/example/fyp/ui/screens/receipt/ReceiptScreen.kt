package com.example.fyp.ui.screens.receipt

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fyp.components.receipt.AddReceiptButton
import com.example.fyp.components.receipt.WelcomeText
import com.example.fyp.components.receipt.AmountPicker
import com.example.fyp.components.receipt.DescriptionInput
import com.example.fyp.components.receipt.MerchantInput
import com.example.fyp.data.ReceiptModel
import java.util.Date
import kotlin.random.Random

@Composable
fun ReceiptScreen(modifier: Modifier = Modifier,
                  receipts: SnapshotStateList<ReceiptModel>
) {

    var merchant by remember { mutableStateOf("") }
    var amount by remember { mutableIntStateOf(10) }
    var description by remember { mutableStateOf("Go Homer!") }

    Column {
        Column(
            modifier = modifier.padding(
                start = 24.dp,
                end = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(30.dp),
        ) {
            WelcomeText()
            Row(
                verticalAlignment = Alignment.CenterVertically,
            )
            {
                Spacer(modifier.weight(1f))
                AmountPicker(
                    onPaymentAmountChange = { amount = it }
                )
            }
            MerchantInput (
                modifier = modifier,
                onMessageChange = {merchant = it}
            )
            DescriptionInput (
                modifier = modifier,
                onMessageChange = { description = it }
            )
            AddReceiptButton (
                modifier = modifier,
                receipt = ReceiptModel(
                    id = Random.nextInt(1, 100000),
                    merchant = merchant,
                    amount = amount.toFloat(),
                    dateCreated = Date(),
                    description = description,
                    userId = Random.nextInt(1, 100000)),
                receipts = receipts,
            )
        }
    }
}

