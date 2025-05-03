package com.example.fyp.ui.screens.reminder

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.fyp.data.models.PrescriptionReminderModel
import com.example.fyp.data.utils.ReminderWorker
import com.example.fyp.firebase.prescription.PrescriptionReminderRepository
import com.example.fyp.firebase.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val repository: PrescriptionReminderRepository,
    private val authService: AuthService
) : ViewModel() {

    private val _reminders = MutableStateFlow<List<PrescriptionReminderModel>>(emptyList())
    val reminders: StateFlow<List<PrescriptionReminderModel>> = _reminders.asStateFlow()

    var isErr = mutableStateOf(false)
    var isLoading = mutableStateOf(false)
    var error = mutableStateOf<Exception?>(null)

    init {
        getReminders()
    }

    fun getReminders() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val email = authService.email
                if (!email.isNullOrEmpty()) {
                    repository.getReminders { items ->
                        _reminders.value = items
                        isErr.value = false
                        isLoading.value = false
                    }
                } else {
                    Timber.e("ReminderViewModel: userId is null, skipping getReminders()")
                    isLoading.value = false
                }
            } catch (e: Exception) {
                isErr.value = true
                isLoading.value = false
                error.value = e
                Timber.e("ReminderViewModel Error: ${e.message}")
            }
        }
    }

    fun addReminder(context: Context, baseReminder: PrescriptionReminderModel, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.addReminder(baseReminder) { success ->
                if (success) {
                    // Refresh to get the Firestore-assigned ID
                    getReminders()

                    // Delay to ensure state updates before finding the reminder
                    viewModelScope.launch {
                        delay(500)
                        val syncedReminder = _reminders.value.find {
                            it.prescriptionName == baseReminder.prescriptionName &&
                                    it.pickupDate == baseReminder.pickupDate
                        }

                        if (syncedReminder != null && !syncedReminder.id.isNullOrBlank()) {
                            scheduleReminder(context, syncedReminder)
                        } else {
                            Timber.e("ReminderViewModel: Failed to find synced reminder with ID")
                        }
                    }
                }
                onResult(success)
            }
        }
    }

    fun deleteReminder(reminderId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.deleteReminder(reminderId) { success ->
                if (success) getReminders()
                onResult(success)
            }
        }
    }

    fun scheduleReminder(context: Context, reminder: PrescriptionReminderModel) {
        val triggerTimeMillis =
            reminder.pickupDate - reminder.reminderDaysBefore * 24 * 60 * 60 * 1000L
        val delay = triggerTimeMillis - System.currentTimeMillis()

        if (delay <= 0 || reminder.id.isNullOrBlank()) {
            Timber.e("ReminderViewModel: Reminder not scheduled. Invalid delay or missing ID.")
            return
        }

        val inputData = workDataOf(
            "prescriptionName" to reminder.prescriptionName,
            "reminderId" to reminder.id
        )

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
        Timber.i("ReminderViewModel: Scheduled reminder '${reminder.prescriptionName}' in ${delay / 1000}s")
    }
}
