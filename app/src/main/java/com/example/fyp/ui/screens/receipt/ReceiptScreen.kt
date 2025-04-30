package com.example.fyp.ui.screens.receipt

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.ui.components.receipt.AddReceiptButton
import com.example.fyp.ui.components.receipt.WelcomeText
import com.example.fyp.ui.components.receipt.AmountPicker
import com.example.fyp.ui.components.receipt.DescriptionInput
import com.example.fyp.ui.components.receipt.MerchantInput
import com.example.fyp.data.models.ReceiptModel
import com.example.fyp.ui.screens.report.ReportViewModel
import java.util.Date

@Composable
fun ReceiptScreen(modifier: Modifier = Modifier,
                  reportViewModel: ReportViewModel = hiltViewModel()
) {

    val receipts = reportViewModel.uiReceipts.collectAsState().value

    var merchant by remember { mutableStateOf("") }
    var amount by remember { mutableIntStateOf(10) }
    var description by remember { mutableStateOf("Go Homer!") }
    var email by remember { mutableStateOf("") }


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
                onMessageChange = { merchant = it}
            )
            DescriptionInput (
                modifier = modifier,
                onMessageChange = { description = it }
            )
            AddReceiptButton (
                modifier = modifier,
                receipt = ReceiptModel(
                    merchant = merchant,
                    amount = amount.toFloat(),
                    dateCreated = Date(),
                    description = description,
                    email = email,
                    )
            )
        }
    }
}

