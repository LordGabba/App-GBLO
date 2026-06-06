package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CategoryEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(private val repository: FinanceRepository) : ViewModel() {

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val profile: StateFlow<UserProfileEntity> = repository.userProfile
        .map { it ?: UserProfileEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    fun addTransaction(
        title: String,
        amount: Double,
        date: Long,
        categoryName: String,
        type: String,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val signedAmount = if (type == "DESPESA") -Math.abs(amount) else Math.abs(amount)
            repository.insertTransaction(
                TransactionEntity(
                    title = title,
                    amount = signedAmount,
                    date = date,
                    categoryName = categoryName,
                    type = type,
                    notes = notes
                )
            )
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun addCategory(name: String, colorHex: String, iconName: String) {
        viewModelScope.launch {
            repository.insertCategory(CategoryEntity(name = name, colorHex = colorHex, iconName = iconName))
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun updateProfile(name: String, currencySymbol: String, useDarkTheme: Boolean) {
        viewModelScope.launch {
            repository.updateProfile(
                UserProfileEntity(
                    id = 1,
                    userName = name,
                    currencySymbol = currencySymbol,
                    useDarkTheme = useDarkTheme
                )
            )
        }
    }
}

class FinanceViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
