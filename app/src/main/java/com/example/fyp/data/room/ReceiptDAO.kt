package com.example.fyp.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fyp.data.ReceiptModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDAO {
    @Query("SELECT * FROM receiptmodel")
    fun getAll(): Flow<List<ReceiptModel>>

    @Insert
    suspend fun insert(receipt: ReceiptModel)

    @Query("UPDATE receiptmodel SET description=:message WHERE id = :id")
    suspend fun update(id: Long, message:String)

    @Delete
    suspend fun delete(receipt: ReceiptModel)

    @Query("SELECT * FROM receiptmodel WHERE id=:id")
    fun get(id: Long): Flow<ReceiptModel>

}
