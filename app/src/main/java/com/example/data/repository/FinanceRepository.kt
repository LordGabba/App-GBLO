package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.TransactionDao
import com.example.data.local.UserProfileDao
import com.example.data.model.CategoryEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val userProfileDao: UserProfileDao
) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    val userProfile: Flow<UserProfileEntity?> = userProfileDao.getUserProfile()

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionById(id: Int) {
        transactionDao.deleteTransactionById(id)
    }

    suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        userProfileDao.insertOrUpdateProfile(profile)
    }

    suspend fun seedDatabase() {
        // Seeding standard categories if list is empty
        val currentCategories = allCategories.firstOrNull()
        if (currentCategories.isNullOrEmpty()) {
            val defaults = listOf(
                CategoryEntity("Salário", "#4CAF50", "AttachMoney"),
                CategoryEntity("Alimentação", "#FF9800", "Restaurant"),
                CategoryEntity("Transporte", "#2196F3", "DirectionsCar"),
                CategoryEntity("Lazer", "#9C27B0", "SportsEsports"),
                CategoryEntity("Saúde", "#F44336", "LocalHospital"),
                CategoryEntity("Educação", "#00BCD4", "School"),
                CategoryEntity("Outros", "#9E9E9E", "MoreHoriz")
            )
            categoryDao.insertCategories(defaults)
        }

        // Seeding a default user profile if none exists
        val profile = userProfile.firstOrNull()
        if (profile == null) {
            userProfileDao.insertOrUpdateProfile(UserProfileEntity())
        }
    }
}
