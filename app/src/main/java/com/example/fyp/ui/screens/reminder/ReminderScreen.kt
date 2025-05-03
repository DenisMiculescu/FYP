package com.example.fyp.ui.screens.reminder

import android.Manifest
import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val reminders by viewModel.reminders.collectAsState()

    var prescriptionName by remember { mutableStateOf("") }
    var reminderDays by remember { mutableStateOf("3") }
    var pickupDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }

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

    val calendar = Calendar.getInstance()
    calendar.timeInMillis = pickupDateMillis
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            pickupDateMillis = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(Unit) {
        viewModel.getReminders()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RemindertText(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.reminderTitle)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Add Reminder", style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                elevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = prescriptionName,
                        onValueChange = { prescriptionName = it },
                        label = { Text("Prescription Name") },
                        leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            focusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = reminderDays,
                        onValueChange = { reminderDays = it },
                        label = { Text("Remind how many days before pickup?") },
                        leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            focusedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            cursorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Select Pickup Date")
                    }

                    Text(
                        text = "Pickup Date: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(pickupDateMillis))}",
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val reminder = PrescriptionReminderModel(
                                prescriptionName = prescriptionName,
                                pickupDate = pickupDateMillis, // use to test: System.currentTimeMillis() + 1 * 60 * 1000,
                                reminderDaysBefore = reminderDays.toIntOrNull() ?: 3
                            )
                            viewModel.addReminder(context, reminder) { success ->
                                Toast.makeText(context, if (success) "Reminder added" else "Failed", Toast.LENGTH_SHORT).show()
                                if (success) {
                                    prescriptionName = ""
                                    reminderDays = "3"
                                }
                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save Reminder")
                    }
                }
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                ShowLoader("Loading Reminders...")
            }

            if (reminders.isEmpty() && !isError) {
                Centre(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp)
                ) {
                    androidx.compose.material3.Text(
                        color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 26.sp,
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.reminder_empty)
                    )
                }
            }


            if (reminders.isNotEmpty() && !isError) {
                RemindertText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    text = stringResource(R.string.reminderSubHeading)
                )
            }
        }

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

    val formattedPickupDate = remember(reminder.pickupDate) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(reminder.pickupDate))
    }

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
                    Text(
                        text = reminder.prescriptionName,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pickup Date: $formattedPickupDate",
                        style = MaterialTheme.typography.body1
                    )
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
