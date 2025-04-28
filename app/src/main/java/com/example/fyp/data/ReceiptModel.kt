package com.example.fyp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity
data class ReceiptModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val _id: String = "N/A",
    @SerializedName("merchant")
    val merchant: String = "N/A",
    @SerializedName("amount")
    val amount: Float = 0.00F,
    @SerializedName("datecreated")
    val dateCreated: Date = Date(),
    var description: String = "MESSAGE!"
)

