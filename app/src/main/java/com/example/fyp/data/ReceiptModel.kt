package com.example.fyp.data

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class ReceiptModel(
    @DocumentId val _id: String = "N/A",
    var merchant: String = "N/A",
    var amount: Float = 0.00F,
    var dateCreated: Date = Date(),
    val dateModified: Date = Date(),
    var description: String = "MESSAGE!",
    var email: String = "joe@bloggs.com",
    var imageUri: String = ""
)
