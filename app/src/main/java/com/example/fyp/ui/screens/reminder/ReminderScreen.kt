package com.example.fyp.ui.screens.reminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fyp.R
import com.example.fyp.data.models.PrescriptionReminderModel
import com.example.fyp.ui.components.general.Centre
import com.example.fyp.ui.components.general.ShowLoader
import com.example.fyp.ui.components.reminder.RemindertText


@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val reminders by viewModel.reminders.collectAsState()

    var prescriptionName by remember { mutableStateOf("") }
    var reminderDays by remember { mutableStateOf("3") }

    val isError = viewModel.isErr.value
    val isLoading = viewModel.isLoading.value

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "Notification permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RemindertText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            text = stringResource(R.string.reminderTitle)
        )

        Text("Add Reminder", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = prescriptionName,
            onValueChange = { prescriptionName = it },
            label = { Text("Prescription Name") },
            leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                focusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = reminderDays,
            onValueChange = { reminderDays = it },
            label = { Text("Remind how many days before pickup?") },
            leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                focusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val reminder = PrescriptionReminderModel(
                    prescriptionName = prescriptionName,
                    pickupDate = System.currentTimeMillis() + 2 * 60 * 1000, // test in 2 mins
                    reminderDaysBefore = 0 // or reminderDays.toIntOrNull() ?: 3
                )


                viewModel.addReminder(reminder) { success ->
                    Toast.makeText(context, if (success) "Reminder added" else "Failed", Toast.LENGTH_SHORT).show()
                    if (success) {
                        viewModel.scheduleReminder(context, reminder)
                        prescriptionName = ""
                        reminderDays = "3"
                        viewModel.getReminders()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colors.background
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Reminder")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            ShowLoader("Loading Reminders...")
        }

        if (reminders.isEmpty() && !isError) {
            Centre(Modifier.fillMaxSize()) {
                androidx.compose.material3.Text(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.reminder_empty)
                )
            }
        }

        if (!isError) {
            RemindertText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.reminderSubHeading)
            )

            LazyColumn {
                items(reminders, key = { it.id }) { reminder ->
                    ReminderSwipeItem(
                        reminder = reminder,
                        onDelete = { id ->
                            viewModel.deleteReminder(id) { success ->
                                if (!success) {
                                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReminderSwipeItem(reminder: PrescriptionReminderModel, onDelete: (String) -> Unit) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                onDelete(reminder.id)
            }
            true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = MaterialTheme.colors.error
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        },
        dismissContent = {
            Card(
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = reminder.prescriptionName, style = MaterialTheme.typography.subtitle1)
                    Text(
                        text = "Notify ${reminder.reminderDaysBefore} day(s) before pickup",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    )
}
