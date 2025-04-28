package com.example.fyp.components.receipt

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.example.fyp.data.ReceiptModel
import com.example.fyp.ui.screens.receipt.ReceiptViewModel
import com.example.fyp.ui.screens.report.ReportViewModel
import timber.log.Timber


@Composable
fun AddReceiptButton(
    modifier: Modifier = Modifier,
    receipt: ReceiptModel,
    receiptViewModel: ReceiptViewModel = hiltViewModel(),
    reportViewModel: ReportViewModel = hiltViewModel(),
) {

    val receipts = reportViewModel.uiReceipts.collectAsState().value

    Row {
        Button(
            onClick = {
                receiptViewModel.insert(receipt)
                Timber.i("Receipt info : $receipts")
                Timber.i("Receipt List info : ${receipts.toList()}")
            },
            elevation = ButtonDefaults.buttonElevation(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Receipt")
            Spacer(modifier.width(width = 4.dp))
            Text(
                text = stringResource(R.string.addButton),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Spacer(modifier.weight(1f))
        Text(

            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                ) {
                    append(stringResource(R.string.total) + " €")
                }

            })
    }
}
