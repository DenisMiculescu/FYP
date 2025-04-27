package com.example.fyp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fyp.data.ReceiptModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDAO {
    @Query("SELECT * FROM receiptmodel")
    fun getAll(): Flow<List<ReceiptModel>>

    @Insert
    suspend fun insert(receipt: ReceiptModel)

    @Update
    suspend fun update(receipt: ReceiptModel)

    @Delete
    suspend fun delete(receipt: ReceiptModel)
}
