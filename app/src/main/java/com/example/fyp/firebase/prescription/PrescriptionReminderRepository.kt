package com.example.fyp.firebase.prescription

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.fyp.data.models.PrescriptionReminderModel
import javax.inject.Inject

class PrescriptionReminderRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userId: String
        get() = auth.currentUser?.uid ?: ""

    fun addReminder(reminder: PrescriptionReminderModel, onResult: (Boolean) -> Unit) {
        val docId = db.collection("prescription_reminders").document().id
        val reminderWithId = reminder.copy(id = docId, userId = userId)

        db.collection("prescription_reminders")
            .document(docId)
            .set(reminderWithId)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getReminders(onResult: (List<PrescriptionReminderModel>) -> Unit) {
        db.collection("prescription_reminders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val reminders = result.documents.mapNotNull { it.toObject(PrescriptionReminderModel::class.java) }
                onResult(reminders)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun deleteReminder(reminderId: String, onResult: (Boolean) -> Unit) {
        db.collection("prescription_reminders")
            .document(reminderId)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

}
