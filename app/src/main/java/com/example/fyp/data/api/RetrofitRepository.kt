package com.example.fyp.data.api

import com.example.fyp.data.ReceiptModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RetrofitRepository @Inject
constructor(private val serviceApi: ReceiptService)  {

    suspend fun getAll(email: String): List<ReceiptModel>
    {
        return withContext(Dispatchers.IO) {
            val receipts = serviceApi.getall(email)
            receipts.body() ?: emptyList()
        }
    }

    suspend fun get(email: String, id: String): List<ReceiptModel>
    {
        return withContext(Dispatchers.IO) {
            val receipt = serviceApi.get(email, id)
            receipt.body() ?: emptyList()
        }
    }

    suspend fun insert(email: String, receipt: ReceiptModel) : ReceiptWrapper
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.post(email, receipt)
            wrapper
        }
    }

    suspend fun update(email: String, receipt: ReceiptModel) : ReceiptWrapper
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.put(email, receipt.id.toString(),receipt)
            wrapper
        }
    }

    suspend fun delete(email: String, receipt: ReceiptModel): Boolean {
        return withContext(Dispatchers.IO) {
            val response = serviceApi.delete(email.lowercase(), receipt.id.toString())
            response.isSuccessful
        }
    }
}
