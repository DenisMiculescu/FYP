package com.example.fyp.data.models

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class ReceiptModel(
    @DocumentId val _id: String = "N/A",
    var dateCreated: Date = Date(),
    val dateModified: Date = Date(),
    var email: String = "joe@bloggs.com",
    var photoUri: String = "",

    var amount: Float = 0.00F,
    var merchant: String = "N/A",
    var receiptImageUrl: String = "",
    var date: String = "",
    val items: List<String> = emptyList()
)
