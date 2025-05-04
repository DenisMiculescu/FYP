package com.example.fyp.data.models

data class PrescriptionReminderModel(
    val id: String = "",
    val userId: String = "",
    val prescriptionName: String = "",
    val pickupDate: Long = 0L,
    val reminderDaysBefore: Int = 3,
)


