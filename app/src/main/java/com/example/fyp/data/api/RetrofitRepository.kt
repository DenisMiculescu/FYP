package com.example.fyp.data.api

import com.example.fyp.data.ReceiptModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class RetrofitRepository @Inject
constructor(private val serviceApi: ReceiptService)  {

    suspend fun getAll(email: String): List<ReceiptModel>
    {
        return withContext(Dispatchers.IO) {
            val receipts = serviceApi.getall(email)

            if (receipts.isSuccessful) {
                Timber.i("RetrofitDebug: Response body: ${receipts.body()}",)
            } else {
                Timber.e("RetrofitDebug: Error body: ${receipts.errorBody()?.string()}")
            }

            receipts.body() ?: emptyList()
        }
    }

    suspend fun get(email: String, id: String): ReceiptModel
    {
        return withContext(Dispatchers.IO) {
            val receipt = serviceApi.get(email, id)
            receipt.body()!!
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
            val wrapper = serviceApi.put(email, receipt.id, receipt)
            wrapper
        }
    }

    suspend fun delete(email: String, receipt: ReceiptModel): Response<Void>
    {
        return withContext(Dispatchers.IO) {
            val wrapper = serviceApi.delete(email, receipt.id)
            wrapper
        }
    }
}
