package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryEntity
import com.example.data.model.TransactionEntity
import com.example.ui.utils.FinanceUtils
import com.example.ui.viewmodel.FinanceViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(viewModel: FinanceViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val profile by viewModel.profile.collectAsState()

    // Calculate aggregated parameters
    val expenses = transactions.filter { it.type == "DESPESA" }
    val totalExpenses = expenses.sumOf { Math.abs(it.amount) }

    val totalIncome = transactions.filter { it.type == "RECEITA" }.sumOf { it.amount }

    // Map Category to sum of absolute expenses
    val categoryExpenses = expenses.groupBy { it.categoryName }
        .mapValues { (_, txList) -> txList.sumOf { Math.abs(it.amount) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Relatórios",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary KPI Section Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Resumo Financeiro Total",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Recebido", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                FinanceUtils.formatCurrency(totalIncome, profile.currencySymbol),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Gasto", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                FinanceUtils.formatCurrency(totalExpenses, profile.currencySymbol),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }
                    }

                    // Simple Horizontal Balance Split Bar indicator
                    val totalSum = totalIncome + totalExpenses
                    if (totalSum > 0) {
                        val incomePct = (totalIncome / totalSum).toFloat()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(Color(0xFFC62828), RoundedCornerShape(4.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(incomePct.coerceIn(0.01f, 0.99f))
                                    .background(Color(0xFF2E7D32), RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight((1f - incomePct).coerceIn(0.01f, 0.99f))
                                    .background(Color(0xFFC62828), RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                            )
                        }
                    }
                }
            }

            // Pie/Donut Distribution Graph
            if (totalExpenses == 0.0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.PieChart,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nenhuma Despesa Encontrada",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Adicione lançamentos do tipo 'Despesa' para gerar a análise de pizza por categoria.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("expenses_report_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Distribuição de Despesas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        // Drawing our Donut Pie Chart on Canvas
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(180.dp)
                        ) {
                            Canvas(modifier = Modifier.size(160.dp)) {
                                var startAngle = 0f
                                categoryExpenses.forEach { (catName, amount) ->
                                    val catEntity = categories.firstOrNull { it.name == catName }
                                    val color = FinanceUtils.parseColor(catEntity?.colorHex ?: "#9E9E9E")
                                    val sweep = (amount / totalExpenses * 360f).toFloat()

                                    drawArc(
                                        color = color,
                                        startAngle = startAngle,
                                        sweepAngle = sweep,
                                        useCenter = false,
                                        style = Stroke(width = 30.dp.toPx(), cap = StrokeCap.Round),
                                        size = Size(size.width, size.height)
                                    )
                                    startAngle += sweep
                                }
                            }

                            // Inner Ring Info Header
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Total Saídas",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = FinanceUtils.formatCurrency(totalExpenses, profile.currencySymbol),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Categories breakdown Lists
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            categoryExpenses.forEach { (catName, amount) ->
                                val catEntity = categories.firstOrNull { it.name == catName }
                                val color = FinanceUtils.parseColor(catEntity?.colorHex ?: "#9E9E9E")
                                val pct = ((amount / totalExpenses) * 100).toInt()

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(color, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = catName,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "($pct%)",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text = FinanceUtils.formatCurrency(amount, profile.currencySymbol),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp)) // Padding above sticky Navigation Bar
        }
    }
}
