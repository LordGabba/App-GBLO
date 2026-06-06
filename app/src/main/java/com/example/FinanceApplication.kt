package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.local.AppDatabase
import com.example.data.repository.FinanceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FinanceApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "finance_database"
        ).build()
    }

    val repository: FinanceRepository by lazy {
        FinanceRepository(
            transactionDao = database.transactionDao(),
            categoryDao = database.categoryDao(),
            userProfileDao = database.userProfileDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Seed database off the main thread
        CoroutineScope(Dispatchers.IO).launch {
            repository.seedDatabase()
        }
    }
}
