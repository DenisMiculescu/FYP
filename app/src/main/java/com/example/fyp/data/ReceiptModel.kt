package com.example.fyp.data

import java.util.Date

data class ReceiptModel(val id: Int,
                        val merchant: String,
                        val amount: Float,
                        val dateCreated: Date,
                        val userId: Int,
                        val description: String
)

