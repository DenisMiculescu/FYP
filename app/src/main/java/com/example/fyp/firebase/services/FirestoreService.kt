package com.example.fyp.firebase.services

import android.net.Uri
import com.example.fyp.data.models.ReceiptModel
import kotlinx.coroutines.flow.Flow

typealias Receipt = ReceiptModel
typealias Receipts = Flow<List<Receipt>>

interface FirestoreService {

    suspend fun getAll(email: String) : Receipts
    suspend fun get(email: String, receiptId: String) : Receipt?
    suspend fun insert(email: String, receipt: Receipt)
    suspend fun update(email: String, receipt: Receipt)
    suspend fun delete(email: String, receiptId: String)

    suspend fun updatePhotoUris(email: String, uri: Uri)


}
