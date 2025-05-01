package com.example.fyp.firebase.database

import android.net.Uri
import com.example.fyp.data.rules.Constants.RECEIPT_COLLECTION
import com.example.fyp.data.rules.Constants.USER_EMAIL
import com.example.fyp.firebase.services.AuthService
import com.example.fyp.firebase.services.FirestoreService
import com.example.fyp.firebase.services.Receipt
import com.example.fyp.firebase.services.Receipts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Date
import javax.inject.Inject


class FirestoreRepository
@Inject constructor(private val auth: AuthService,
                    private val firestore: FirebaseFirestore
) : FirestoreService {

    override suspend fun getAll(email: String): Receipts {
        return firestore.collection(RECEIPT_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .dataObjects()
    }
    override suspend fun get(email: String,
                             receiptId: String): Receipt? {
        return firestore.collection(RECEIPT_COLLECTION)
            .document(receiptId).get().await().toObject()
    }

    override suspend fun insert(email: String, receipt: Receipt) {
        val receiptWithEmailAndImage =
            receipt.copy(
                email = email,
                photoUri = auth.customPhotoUri?.toString().orEmpty()
            )

        firestore.collection(RECEIPT_COLLECTION)
            .add(receiptWithEmailAndImage)
            .await()
    }

    override suspend fun update(email: String,
                                receipt: Receipt) {
        val receiptWithModifiedDate =
            receipt.copy(dateModified = Date())

        firestore.collection(RECEIPT_COLLECTION)
            .document(receipt._id)
            .set(receiptWithModifiedDate).await()
    }

    override suspend fun delete(email: String,
                                receiptId: String) {
        firestore.collection(RECEIPT_COLLECTION)
            .document(receiptId)
            .delete().await()
    }

    override suspend fun updatePhotoUris(email: String, uri: Uri) {

        firestore.collection(RECEIPT_COLLECTION)
            .whereEqualTo(USER_EMAIL, email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Timber.i("FSR Updating ID ${document.id}")
                    firestore.collection(RECEIPT_COLLECTION)
                        .document(document.id)
                        .update("imageUri", uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Timber.i("Error $exception")
            }
    }
}
