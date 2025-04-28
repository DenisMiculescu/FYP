package com.example.fyp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ReceiptModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val merchant: String = "N/A",
    val amount: Float = 0.00F,
    val dateCreated: Date = Date(),
    val userId: Int = 1,
    var description: String = "MESSAGE!"
)

