package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.model.CategoryEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserProfileEntity

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, UserProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userProfileDao(): UserProfileDao
}
