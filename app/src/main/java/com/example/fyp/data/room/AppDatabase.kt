package com.example.fyp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fyp.data.ReceiptModel

@Database(entities = [ReceiptModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getReceiptDAO(): ReceiptDAO
}
