package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransactionEntity
import com.example.ui.theme.WealthAmber
import com.example.ui.theme.WealthBlue
import com.example.ui.theme.WealthBlueGlow
import com.example.ui.theme.WealthDanger
import com.example.ui.theme.WealthGreen
import com.example.ui.theme.WealthGreenSoft
import com.example.ui.utils.FinanceUtils
import com.example.ui.viewmodel.FinanceViewModel
import java.util.Calendar
import kotlin.math.absoluteValue

private data class GoalUi(
    val name: String,
    val percent: Int,
    val icon: ImageVector,
    val color: Color
)

private data class ToolUi(
    val label: String,
    val icon: ImageVector,
    val background: Color,
    val foreground: Color
)

@Composable
fun DashboardScreen(
    viewModel: FinanceViewModel,
    onNavigateToTransactions: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val totalBalance = transactions.sumOf { it.amount }
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonthTransactions = transactions.filter {
        val txCal = Calendar.getInstance().apply { timeInMillis = it.date }
        txCal.get(Calendar.MONTH) == currentMonth && txCal.get(Calendar.YEAR) == currentYear
    }
    val currentMonthIncome = currentMonthTransactions
        .filter { it.type == "RECEITA" }
        .sumOf { it.amount }
    val currentMonthExpense = currentMonthTransactions
        .filter { it.type == "DESPESA" }
        .sumOf { it.amount }
        .absoluteValue
    val investmentsEstimate = (totalBalance * 0.4).coerceAtLeast(0.0)
    val reserveEstimate = (currentMonthIncome - currentMonthExpense).coerceAtLeast(0.0)
    val budgetLimit = if (currentMonthIncome > 0.0) currentMonthIncome else 5000.0
    val budgetUsage = if (budgetLimit > 0.0) {
        (currentMonthExpense / budgetLimit).coerceIn(0.0, 1.0).toFloat()
    } else {
        0f
    }
    val recentTransactions = transactions.take(4)
    val healthScore = (100 - (budgetUsage * 45)).toInt().coerceIn(55, 96)
    val firstLetter = profile.userName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "L"

    val goals = listOf(
        GoalUi("Casa", 72, Icons.Default.AccountBalance, WealthBlue),
        GoalUi("Viagem", 34, Icons.Default.DateRange, WealthGreen),
        GoalUi("Alianças", 58, Icons.Default.Favorite, WealthAmber),
        GoalUi("Reserva", 96, Icons.Default.Savings, Color(0xFF06B6D4))
    )
    val tools = listOf(
        ToolUi("Receitas", Icons.Default.AttachMoney, WealthGreenSoft, WealthGreen),
        ToolUi("Despesas", Icons.Default.TrendingDown, Color(0xFFFFF1F2), WealthDanger),
        ToolUi("Cartões", Icons.Default.CreditCard, Color(0xFFEFF6FF), WealthBlue),
        ToolUi("Metas", Icons.Default.Flag, Color(0xFFFFFBEB), WealthAmber),
        ToolUi("Categorias", Icons.Default.Category, Color(0xFFF5F3FF), Color(0xFF7C3AED)),
        ToolUi("Relatórios", Icons.Default.Assessment, Color(0xFFECFEFF), Color(0xFF0891B2)),
        ToolUi("Planejamento", Icons.Default.DateRange, Color(0xFFFDF2F8), Color(0xFFDB2777)),
        ToolUi("Assistente", Icons.Default.Settings, Color(0xFFFAE8FF), Color(0xFFC026D3))
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DashboardHeader(
                firstLetter = firstLetter,
                userName = profile.userName,
                onNavigateToReports = onNavigateToReports
            )

            WealthCard(
                totalBalance = totalBalance,
                currencySymbol = profile.currencySymbol,
                monthIncome = currentMonthIncome,
                monthExpense = currentMonthExpense
            )

            FinancialHealthCard(score = healthScore, budgetUsage = budgetUsage)

            SummaryGrid(
                currencySymbol = profile.currencySymbol,
                income = currentMonthIncome,
                expense = currentMonthExpense,
                investments = investmentsEstimate,
                reserve = reserveEstimate
            )

            GoalsSection(goals = goals)
            ToolsSection(tools = tools, onReports = onNavigateToReports)
            ProjectionSection(totalBalance = totalBalance, currencySymbol = profile.currencySymbol)
            RecentTransactionsSection(
                transactions = recentTransactions,
                currencySymbol = profile.currencySymbol,
                onNavigateToTransactions = onNavigateToTransactions,
                onDelete = viewModel::deleteTransaction
            )

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun DashboardHeader(
    firstLetter: String,
    userName: String,
    onNavigateToReports: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(listOf(WealthBlue, WealthBlueGlow, WealthGreen)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstLetter,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Olá, $userName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Seu patrimônio está crescendo.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = onNavigateToReports)
                .testTag("dashboard_reports_button"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Notifications,
                contentDescription = "Abrir relatórios",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp)
                    .size(8.dp)
                    .background(WealthGreen, CircleShape)
            )
        }
    }
}

@Composable
private fun WealthCard(
    totalBalance: Double,
    currencySymbol: String,
    monthIncome: Double,
    monthExpense: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("balance_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(listOf(WealthBlue, WealthBlueGlow, WealthGreen)))
                .padding(22.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PATRIMÔNIO TOTAL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White.copy(alpha = 0.72f),
                            letterSpacing = 1.1.sp
                        )
                        Text(
                            text = FinanceUtils.formatCurrency(totalBalance, currencySymbol),
                            fontSize = 32.sp,
                            lineHeight = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        GrowthPill()
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.16f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Sparkline(
                    points = listOf(12f, 18f, 15f, 22f, 19f, 26f, 24f, 30f, 28f, 34f, 32f, 38f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MiniMetric(
                        label = "Receitas",
                        value = FinanceUtils.formatCurrency(monthIncome, currencySymbol),
                        icon = Icons.Default.TrendingUp,
                        tint = Color(0xFF86EFAC),
                        modifier = Modifier.weight(1f)
                    )
                    MiniMetric(
                        label = "Despesas",
                        value = FinanceUtils.formatCurrency(monthExpense, currencySymbol),
                        icon = Icons.Default.TrendingDown,
                        tint = Color(0xFFFCA5A5),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun GrowthPill() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(WealthGreen.copy(alpha = 0.24f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF86EFAC), modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text("+12,4% este ano", color = Color(0xFFBBF7D0), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MiniMetric(
    label: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color.White.copy(alpha = 0.14f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, color = Color.White.copy(alpha = 0.72f), fontSize = 10.sp)
            Text(
                value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FinancialHealthCard(score: Int, budgetUsage: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreRing(score = score)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = WealthGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Saúde Financeira",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                HealthLine("Reserva de emergência", if (score > 80) "Excelente" else "Boa", WealthGreen)
                HealthLine("Dívidas", if (budgetUsage < 0.8f) "Controladas" else "Atenção", if (budgetUsage < 0.8f) MaterialTheme.colorScheme.onSurface else WealthDanger)
                HealthLine("Investimentos", "Crescendo", WealthBlue)
            }
        }
    }
}

@Composable
private fun HealthLine(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Text(value, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ScoreRing(score: Int) {
    Box(
        modifier = Modifier.size(82.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 7.dp.toPx()
            val inset = stroke / 2f
            drawArc(
                color = Color(0xFFE5E7EB),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = Size(size.width - stroke, size.height - stroke),
                topLeft = Offset(inset, inset)
            )
            drawArc(
                color = WealthGreen,
                startAngle = -90f,
                sweepAngle = 360f * (score / 100f),
                useCenter = false,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
                size = Size(size.width - stroke, size.height - stroke),
                topLeft = Offset(inset, inset)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(score.toString(), color = MaterialTheme.colorScheme.onSurface, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text("/ 100", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
        }
    }
}

@Composable
private fun SummaryGrid(
    currencySymbol: String,
    income: Double,
    expense: Double,
    investments: Double,
    reserve: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Receitas do mês", FinanceUtils.formatCurrency(income, currencySymbol), Icons.Default.TrendingUp, WealthGreenSoft, WealthGreen, Modifier.weight(1f))
            SummaryCard("Despesas do mês", FinanceUtils.formatCurrency(expense, currencySymbol), Icons.Default.TrendingDown, Color(0xFFFFF1F2), WealthDanger, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Investimentos", FinanceUtils.formatCurrency(investments, currencySymbol), Icons.Default.TrendingUp, Color(0xFFEFF6FF), WealthBlue, Modifier.weight(1f))
            SummaryCard("Reserva", FinanceUtils.formatCurrency(reserve, currencySymbol), Icons.Default.Savings, Color(0xFFFFFBEB), WealthAmber, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(iconBackground, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(
                value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 17.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun GoalsSection(goals: List<GoalUi>) {
    SectionHeader(title = "Metas Financeiras", action = "Ver todas")
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        goals.forEach { goal ->
            Card(
                modifier = Modifier.widthIn(min = 156.dp).width(168.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(goal.icon, contentDescription = null, tint = goal.color, modifier = Modifier.size(26.dp))
                        Text("${goal.percent}%", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Meta ${goal.name}", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { goal.percent / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(CircleShape),
                        color = goal.color,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolsSection(tools: List<ToolUi>, onReports: () -> Unit) {
    SectionHeader(title = "Ferramentas")
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        tools.chunked(4).forEach { rowTools ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowTools.forEach { tool ->
                    ToolButton(
                        tool = tool,
                        modifier = Modifier.weight(1f),
                        onClick = if (tool.label == "Relatórios") onReports else null
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolButton(tool: ToolUi, modifier: Modifier = Modifier, onClick: (() -> Unit)?) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(tool.background, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(tool.icon, contentDescription = tool.label, tint = tool.foreground, modifier = Modifier.size(25.dp))
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            tool.label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 10.5.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProjectionSection(totalBalance: Double, currencySymbol: String) {
    val base = totalBalance.coerceAtLeast(36000.0)
    val values = listOf(base, base * 2.45, base * 5.65, base * 24.5)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = WealthGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "PROJEÇÃO PATRIMONIAL",
                    color = WealthGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
            Text("Seu futuro em números", color = MaterialTheme.colorScheme.onSurface, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Sparkline(
                points = listOf(36f, 58f, 89f, 140f, 205f, 380f, 600f, 890f),
                lineColor = WealthGreen,
                fillColor = WealthGreen.copy(alpha = 0.15f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                listOf("Hoje", "5 anos", "10 anos", "20 anos").forEachIndexed { index, label ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
                        Text(
                            FinanceUtils.formatCurrency(values[index], currencySymbol),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentTransactionsSection(
    transactions: List<TransactionEntity>,
    currencySymbol: String,
    onNavigateToTransactions: () -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Lançamentos recentes", color = MaterialTheme.colorScheme.onSurface, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = onNavigateToTransactions, modifier = Modifier.testTag("see_all_transactions_button")) {
            Text("Ver todos")
        }
    }

    if (transactions.isEmpty()) {
        EmptyTransactions()
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            transactions.forEach { tx ->
                TransactionItemRow(
                    transaction = tx,
                    currencySymbol = currencySymbol,
                    onDelete = { onDelete(tx) }
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactions() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 28.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Nenhuma transação cadastrada",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Toque no botão central para adicionar um lançamento.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionHeader(title: String, action: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = MaterialTheme.colorScheme.onSurface, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        if (action != null) {
            Text(action, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Sparkline(
    points: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.White,
    fillColor: Color = Color.White.copy(alpha = 0.18f)
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas
        val min = points.minOrNull() ?: 0f
        val max = points.maxOrNull() ?: 1f
        val range = (max - min).takeIf { it > 0f } ?: 1f
        val step = size.width / (points.lastIndex)
        fun yFor(value: Float): Float = size.height - (((value - min) / range) * (size.height - 10.dp.toPx())) - 5.dp.toPx()

        val linePath = Path()
        val areaPath = Path()
        points.forEachIndexed { index, point ->
            val x = index * step
            val y = yFor(point)
            if (index == 0) {
                linePath.moveTo(x, y)
                areaPath.moveTo(x, size.height)
                areaPath.lineTo(x, y)
            } else {
                linePath.lineTo(x, y)
                areaPath.lineTo(x, y)
            }
        }
        areaPath.lineTo(size.width, size.height)
        areaPath.close()
        drawPath(areaPath, fillColor)
        drawPath(linePath, lineColor, style = Stroke(width = 2.6.dp.toPx(), cap = StrokeCap.Round))
        val lastX = points.lastIndex * step
        val lastY = yFor(points.last())
        drawCircle(lineColor, radius = 4.5.dp.toPx(), center = Offset(lastX, lastY))
    }
}

@Composable
fun TransactionItemRow(
    transaction: TransactionEntity,
    currencySymbol: String,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isIncome = transaction.type == "RECEITA"
    val tone = if (isIncome) WealthGreen else WealthDanger
    val toneBackground = if (isIncome) WealthGreenSoft else Color(0xFFFFF1F2)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("transaction_item_${transaction.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(toneBackground, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = FinanceUtils.getIconByName(transaction.categoryName),
                        contentDescription = transaction.categoryName,
                        tint = tone,
                        modifier = Modifier.size(21.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${transaction.categoryName} • ${FinanceUtils.formatDate(transaction.date)}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = if (isIncome) {
                        "+${FinanceUtils.formatCurrency(transaction.amount, currencySymbol)}"
                    } else {
                        FinanceUtils.formatCurrency(transaction.amount, currencySymbol)
                    },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = tone,
                    maxLines = 1
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (transaction.notes.isNotBlank()) {
                        Text(
                            text = "Observações:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = transaction.notes,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excluir", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
