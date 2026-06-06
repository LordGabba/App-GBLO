package com.example.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object FinanceUtils {

    fun formatCurrency(amount: Double, symbol: String): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            val formatted = format.format(amount)
            if (symbol != "R$") {
                formatted.replace("R$", symbol)
            } else {
                formatted
            }
        } catch (e: Exception) {
            val format = NumberFormat.getNumberInstance(Locale("pt", "BR"))
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = 2
            "$symbol ${format.format(amount)}"
        }
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        return sdf.format(Date(timestamp))
    }

    // Convert icon name standard string to material icon
    fun getIconByName(name: String): ImageVector {
        return when (name) {
            "AttachMoney", "Work" -> Icons.Default.AttachMoney
            "Restaurant", "Food" -> Icons.Default.Restaurant
            "DirectionsCar", "Transport" -> Icons.Default.DirectionsCar
            "SportsEsports", "Game" -> Icons.Default.SportsEsports
            "LocalHospital", "Health" -> Icons.Default.LocalHospital
            "School", "Study" -> Icons.Default.School
            "MoreHoriz", "Other" -> Icons.Default.MoreHoriz
            "Home" -> Icons.Default.Home
            "ShoppingBag", "Shopping" -> Icons.Default.ShoppingBag
            "Flight", "Travel" -> Icons.Default.Flight
            "FlashOn", "Energy" -> Icons.Default.FlashOn
            "WaterDrop" -> Icons.Default.WaterDrop
            "Tv" -> Icons.Default.Tv
            "Build" -> Icons.Default.Build
            "HeartBroken" -> Icons.Default.HeartBroken
            "AccountBalance" -> Icons.Default.AccountBalance
            "Savings" -> Icons.Default.Savings
            else -> Icons.Default.Category
        }
    }

    // Colors parsing
    fun parseColor(hex: String): Color {
        return try {
            Color(android.graphics.Color.parseColor(hex))
        } catch (e: Exception) {
            Color(0xFF9E9E9E) // Fallback gray
        }
    }
}
