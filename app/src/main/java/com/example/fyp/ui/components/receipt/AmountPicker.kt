package com.example.fyp.ui.components.receipt

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.ListItemPicker


@Composable
fun AmountPicker(
    onPaymentAmountChange: (Int) -> Unit
) {
    val possibleValues = listOf("10", "20", "50", "100", "500", "1000")
    var pickerValue by remember { mutableStateOf(possibleValues[0]) }

    ListItemPicker(
        label = { it },
        dividersColor = MaterialTheme.colorScheme.primary,
        textStyle = TextStyle(Color.Black,20.sp),
        value = pickerValue,
        onValueChange = {
            pickerValue = it
            onPaymentAmountChange(pickerValue.toInt())
        },
        list = possibleValues
    )
}

