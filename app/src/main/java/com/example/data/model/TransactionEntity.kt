package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,              // Positive for income, negative for expense
    val date: Long,                  // Timestamp in milliseconds
    val categoryName: String,        // Name of the CategoryEntity
    val type: String,                // "RECEITA" (Income) or "DESPESA" (Expense)
    val notes: String = ""
)
