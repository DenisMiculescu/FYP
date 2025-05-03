package com.example.fyp.ui.components.reminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fyp.R

@Composable
fun RemindertText(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier.padding(
            top = 24.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(48.dp)) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.Black
        )
    }
}