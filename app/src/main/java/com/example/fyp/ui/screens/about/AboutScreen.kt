package com.example.fyp.ui.screens.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fyp.R

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.receipt_pic),
            contentDescription = "Receiptly Logo",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )

        Text(
            text = "Receiptly",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Your personal pharmacy receipt and prescription manager. Receiptly helps you store, organize, analyze, and stay reminded of your health expenses and prescriptions.",
            color = Color.White,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center
        )

        Section(title = "Features", content = listOf(
            "Upload and view pharmacy receipts",
            "OCR analysis for receipt data",
            "Google Maps integration for nearby pharmacies",
            "Prescription pickup reminders",
            "User profile management"
        ))

        Section(title = "Technologies Used", content = listOf(
            "Android (Kotlin + Jetpack Compose)",
            "Firebase (Auth, Firestore, Storage)",
            "WorkManager for background tasks",
            "Google Maps & Places SDK",
            "Hilt for dependency injection"
        ))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/DenisMiculescu/FYP"))
                    context.startActivity(githubIntent)
                }
        ) {
            Text(
                text = "📄 View on GitHub",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }

        Text(
            text = "App Version: 1.0.0",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Section(title: String, content: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        content.forEach {
            Text(
                text = "• $it",
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        }
    }
}
