package com.example.fyp.ui.screens.receipt

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.ui.components.receipt.AddReceiptButton
import com.example.fyp.components.receipt.WelcomeText
import com.example.fyp.components.receipt.AmountPicker
import com.example.fyp.components.receipt.DescriptionInput
import com.example.fyp.components.receipt.MerchantInput
import com.example.fyp.data.ReceiptModel
import com.example.fyp.ui.screens.report.ReportViewModel
import java.util.Date
import kotlin.random.Random

@Composable
fun ReceiptScreen(modifier: Modifier = Modifier,
                  reportViewModel: ReportViewModel = hiltViewModel()
) {

    val receipts = reportViewModel.uiReceipts.collectAsState().value

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
                    id = Random.nextLong(1, 100000),
                    merchant = merchant,
                    amount = amount.toFloat(),
                    dateCreated = Date(),
                    description = description,
                    )
            )
        }
    }
}

