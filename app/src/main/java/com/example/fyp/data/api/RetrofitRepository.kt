package com.example.fyp.data.api

import com.example.fyp.data.ReceiptModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitRepository @Inject
constructor(private val serviceApi: ReceiptService)  {

    suspend fun getAll(): List<ReceiptModel>
    {
        return withContext(Dispatchers.IO) {
            val receipts = serviceApi.getall()
            receipts.body() ?: emptyList()
        }
    }

    suspend fun get(id: String): List<ReceiptModel>
    {
        return withContext(Dispatchers.IO) {
            val receipt = serviceApi.get(id)
            receipt.body() ?: emptyList()
        }
    }

    suspend fun insert(receipt: ReceiptModel) : ReceiptWrapper
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.post(receipt)
            wrapper
        }
    }

    suspend fun update(receipt: ReceiptModel) : ReceiptWrapper
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.put(receipt._id,receipt)
            wrapper
        }
    }

    suspend fun delete(receipt: ReceiptModel) : ReceiptWrapper
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.delete(receipt._id)
            wrapper
        }
    }
}
