package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Usuário",
    val currencySymbol: String = "R$",
    val useDarkTheme: Boolean = true
)
