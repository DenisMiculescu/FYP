package com.example.fyp.ui.screens.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.example.fyp.data.models.ReceiptModel
import com.example.fyp.ui.components.report.ReceiptCardList
import com.example.fyp.ui.components.general.Centre
import com.example.fyp.ui.components.general.ShowError
import com.example.fyp.ui.components.general.ShowLoader
import com.example.fyp.ui.components.report.ReportText
import timber.log.Timber

@Composable
fun ReportScreen(
    modifier: Modifier = Modifier,
    reportViewModel: ReportViewModel = hiltViewModel(),
    onClickReceiptDetails: (String) -> Unit,
) {

    val receipts = reportViewModel.uiReceipts.collectAsState().value
    val isError = reportViewModel.isErr.value
    val isLoading = reportViewModel.isLoading.value
    val error = reportViewModel.error.value

    var filterText by rememberSaveable { mutableStateOf("") }
    val filteredReceipts = receipts.filter {
        it.merchant.contains(filterText, ignoreCase = true)
    }

    Timber.i("RS : Receipts List = $receipts")

    Column {
        Column(
            modifier = modifier.padding(
                start = 24.dp,
                end = 24.dp
            ),
        ) {

            if(isLoading) ShowLoader("Loading Receipts...")

            ReportText()

            if (receipts.isEmpty() && !isError)
                Centre(Modifier.fillMaxSize()) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        lineHeight = 34.sp,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.empty_list)
                    )}

            if (receipts.isNotEmpty() && !isError) {
                TextField(
                    value = filterText,
                    onValueChange = { value -> filterText = value },
                    label = { Text("Search Pharmacy") },
                    modifier = Modifier.fillMaxWidth()
                )
                ReceiptCardList(
                    receipts = filteredReceipts,
                    onClickReceiptDetails = onClickReceiptDetails,
                    onDeleteReceipt = { receipt: ReceiptModel ->
                        reportViewModel.deleteReceipt(receipt)
                    },
                )
            }
            if (isError) {
                ShowError(headline = error.message!! + " error...",
                    subtitle = error.toString(),
                    onClick = { reportViewModel.getReceipts() })
            }
        }
    }
}
