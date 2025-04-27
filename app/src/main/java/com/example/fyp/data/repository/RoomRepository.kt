package com.example.fyp.data.repository

import com.example.fyp.data.ReceiptModel
import com.example.fyp.data.room.ReceiptDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject
constructor(private val receiptDAO: ReceiptDAO) {
    fun getAll(): Flow<List<ReceiptModel>>
            = receiptDAO.getAll()

    suspend fun insert(receipt: ReceiptModel)
    { receiptDAO.insert(receipt) }

    suspend fun update(receipt: ReceiptModel)
    { receiptDAO.update(receipt) }

    suspend fun delete(receipt: ReceiptModel)
    { receiptDAO.delete(receipt) }
}
