package com.example.fyp.firebase.database

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

    override suspend fun insert(email: String,
                                receipt: Receipt)
    {
        val receiptWithEmail = receipt.copy(email = email)

        firestore.collection(RECEIPT_COLLECTION)
            .add(receiptWithEmail)
            .await()

    }

    override suspend fun update(email: String,
                                receipt: Receipt)
    {
//        val receiptWithModifiedDate =
//            receipt.copy(dateModified = Date())
//
//        firestore.collection(RECEIPT_COLLECTION)
//            .document(receipt._id)
//            .set(receiptWithModifiedDate).await()
    }

    override suspend fun delete(email: String,
                                receiptId: String)
    {
        firestore.collection(RECEIPT_COLLECTION)
            .document(receiptId)
            .delete().await()
    }
}
