package com.example.fyp.ui.screens.receipt

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fyp.data.models.ReceiptModel
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirestoreService
import com.example.fyp.utils.ReceiptOCRUtils
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _ocrFields = MutableStateFlow<Map<String, Any>>(emptyMap())

    fun insert(receipt: ReceiptModel) = viewModelScope.launch {
        try {
            isLoading.value = true
            repository.insert(authService.email!!, receipt)
            isErr.value = false
        } catch (e: Exception) {
            isErr.value = true
            error.value = e
            Timber.e(e, "Error inserting receipt")
        } finally {
            isLoading.value = false
            Timber.i("Receipt Inserted | Error State: ${isErr.value}")
        }
    }

    fun uploadReceiptFromOCR(context: Context, receiptImageUrl: Uri) {
        viewModelScope.launch {
            try {
                val text = ReceiptOCRUtils.processImage(context, receiptImageUrl)
                text?.let {
                    Timber.i("Raw OCR text:\n$it")
                    val fields = ReceiptOCRUtils.extractFields(it)
                    Timber.i("Extracted fields = $fields")
                    _ocrFields.value = fields

                    val merchant = fields["merchant"] as? String ?: "Unknown Merchant"
                    val amount = (fields["amount"] as? Float)
                        ?: fields["amount"]?.toString()?.replace(",", ".")?.toFloatOrNull()
                        ?: 0f

                    val date = fields["date"] as? String ?: "Unknown Date"
                    val items = fields["items"] as? List<String> ?: emptyList()

                    uploadFullReceipt(receiptImageUrl, merchant, amount, date, items)
                }
            } catch (e: Exception) {
                Timber.e(e, "OCR or upload failed")
            }
        }
    }

    fun uploadFullReceipt(uri: Uri, merchant: String, amount: Float, date: String, items: List<String>) {
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
                    date = date,
                    items = items,
                    receiptImageUrl = downloadUrl.toString()
                )
                Timber.i("OCR receipt upload complete. URL: $downloadUrl")
                viewModelScope.launch {
                    insert(receipt)
                }
            }
            .addOnFailureListener {
                Timber.e(it, "OCR image upload failed")
            }
    }

}
