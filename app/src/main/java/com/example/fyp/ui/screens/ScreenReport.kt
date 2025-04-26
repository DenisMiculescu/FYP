package com.example.fyp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fyp.R
import com.example.fyp.ui.components.report.ReceiptCardList
import com.example.fyp.ui.components.report.ReportText
import com.example.fyp.data.ReceiptModel
import com.example.fyp.ui.components.general.Centre

@Composable
fun ScreenReport(modifier: Modifier = Modifier,
                 receipts: SnapshotStateList<ReceiptModel>
) {

    Column {
        Column(
            modifier = modifier.padding(
                top = 48.dp,
                start = 24.dp,
                end = 24.dp
            ),
        ) {
            ReportText()
            if (receipts.isEmpty())
                Centre(Modifier.fillMaxSize()) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        lineHeight = 34.sp,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.empty_list)
                    )}
            else
                ReceiptCardList(
                    receipts = receipts
                )
        }
    }
}
