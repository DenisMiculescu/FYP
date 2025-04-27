package com.example.fyp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.fyp.data.room.AppDatabase
import com.example.fyp.data.room.ReceiptDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context):
            AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "receipt_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideReceiptDAO(appDatabase: AppDatabase):
            ReceiptDAO = appDatabase.getReceiptDAO()

    @Provides
    fun provideRoomRepository(receiptDAO: ReceiptDAO):
            RoomRepository = RoomRepository(receiptDAO)
}
