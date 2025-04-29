package com.example.fyp.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

@Entity
data class ReceiptModel(

    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),

    @Ignore
    @SerializedName("_id")
    var _id: String = UUID.randomUUID().toString(),

    @SerializedName("merchant")
    var merchant: String = "N/A",

    @SerializedName("amount")
    var amount: Float = 0.00F,

    @SerializedName("datecreated")
    var dateCreated: Date = Date(),

    var description: String = "MESSAGE!",

    var email: String = "joe@bloggs.com"
)
