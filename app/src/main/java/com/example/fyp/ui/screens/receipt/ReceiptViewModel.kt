package com.example.fyp.ui.screens.receipt

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.models.ReceiptModel
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirestoreService
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val repository: FirestoreService,
    private val authService: AuthService
) : ViewModel() {

    var isErr = mutableStateOf(false)
    var error = mutableStateOf(Exception())
    var isLoading = mutableStateOf(false)

    fun insert(receipt: ReceiptModel) = viewModelScope.launch {
        try {
            isLoading.value = true
            repository.insert(authService.email!!, receipt)
            isErr.value = false
            isLoading.value = false
        } catch (e: Exception) {
            isErr.value = true
            error.value = e
            isLoading.value = false
            Timber.e(e, "Error inserting receipt")
        }
        Timber.i("Receipt Inserted: ${error.value.message} | Error State: ${isErr.value}")
    }

    fun uploadReceiptImage(
        uri: Uri,
        merchant: String,
        amount: Float,
        description: String,
        email: String
    ) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileRef = storageRef.child("receipts/${UUID.randomUUID()}.jpg")

        fileRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                fileRef.downloadUrl
            }
            .addOnSuccessListener { downloadUrl ->
                val receipt = ReceiptModel(
                    merchant = merchant,
                    amount = amount,
                    description = description,
                    email = email,
                    receiptImageUrl = downloadUrl.toString(),
                )
                Timber.i("Uploaded image URL: $downloadUrl")
                viewModelScope.launch {
                    repository.insert(authService.email!!, receipt)
                }
            }
            .addOnFailureListener {
                Timber.e(it, "Image upload failed")
            }
    }
}
