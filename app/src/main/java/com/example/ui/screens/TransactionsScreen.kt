package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CategoryEntity
import com.example.data.model.TransactionEntity
import com.example.ui.utils.FinanceUtils
import com.example.ui.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(viewModel: FinanceViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val profile by viewModel.profile.collectAsState()

    // Filter and search states
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("TODOS") } // "TODOS", "RECEITA", "DESPESA"
    var selectedCategoryFilter by remember { mutableStateOf("TODOS") }

    // Dialog state
    var showAddDialog by remember { mutableStateOf(false) }

    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Execute filter logic
    val filteredTransactions = transactions.filter { tx ->
        val matchesSearch = tx.title.contains(searchQuery, ignoreCase = true) ||
                tx.notes.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedTypeFilter == "TODOS" || tx.type == selectedTypeFilter
        val matchesCategory = selectedCategoryFilter == "TODOS" || tx.categoryName == selectedCategoryFilter
        matchesSearch && matchesType && matchesCategory
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lançamentos",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_transaction_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nova Transação")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_bar"),
                placeholder = { Text("Buscar lançamentos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // Filtering Tags Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type Filter: TODOS
                FilterChip(
                    selected = selectedTypeFilter == "TODOS",
                    onClick = { selectedTypeFilter = "TODOS" },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors()
                )

                // Type Filter: RECEITA
                FilterChip(
                    selected = selectedTypeFilter == "RECEITA",
                    onClick = { selectedTypeFilter = "RECEITA" },
                    label = { Text("Receitas") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF2E7D32)
                        )
                    }
                )

                // Type Filter: DESPESA
                FilterChip(
                    selected = selectedTypeFilter == "DESPESA",
                    onClick = { selectedTypeFilter = "DESPESA" },
                    label = { Text("Despesas") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.TrendingDown,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFC62828)
                        )
                    }
                )

                // Category Filter selection dropdown placeholder
                Box {
                    var menuExpanded by remember { mutableStateOf(false) }
                    FilterChip(
                        selected = selectedCategoryFilter != "TODOS",
                        onClick = { menuExpanded = true },
                        label = { Text(if (selectedCategoryFilter == "TODOS") "Filtrar Categoria" else selectedCategoryFilter) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas as Categorias") },
                            onClick = {
                                selectedCategoryFilter = "TODOS"
                                menuExpanded = false
                            }
                        )
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryFilter = category.name
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Results summary helper list
            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Text(
                            "Nenhum lançamento encontrado",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "Limpe os filtros ou busque por outro termo para encontrar lançamentos.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // Cushion above bottom nav bar
                ) {
                    items(filteredTransactions, key = { it.id }) { tx ->
                        TransactionItemRow(
                            transaction = tx,
                            currencySymbol = profile.currencySymbol,
                            onDelete = {
                                viewModel.deleteTransaction(tx)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Lançamento '${tx.title}' excluído.",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal Add Transaction
    if (showAddDialog) {
        AddTransactionDialog(
            categories = categories,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, amount, date, categoryName, type, notes ->
                viewModel.addTransaction(title, amount, date, categoryName, type, notes)
                showAddDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Lançamento '$title' registrado com sucesso!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, date: Long, categoryName: String, type: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()?.name ?: "Outros") }
    var selectedType by remember { mutableStateOf("DESPESA") } // "RECEITA" or "DESPESA"
    var notes by remember { mutableStateOf("") }

    // Validation fields
    var titleError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("add_transaction_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Novo Lançamento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Type Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (selectedType == "RECEITA") Color(0xFF2E7D32) else Color.Transparent)
                            .clickable { selectedType = "RECEITA" }
                            .testTag("add_tx_type_revenue"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Receita",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == "RECEITA") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (selectedType == "DESPESA") Color(0xFFC62828) else Color.Transparent)
                            .clickable { selectedType = "DESPESA" }
                            .testTag("add_tx_type_expense"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Despesa",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedType == "DESPESA") Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Description Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotEmpty()) titleError = null
                    },
                    label = { Text("Nome do lançamento") },
                    isError = titleError != null,
                    supportingText = { titleError?.let { Text(it) } },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_tx_title_input")
                )

                // Amount value Input
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = {
                        // Allow numbers, commas, and dots
                        val clean = it.replace(",", ".")
                        if (clean.all { char -> char.isDigit() || char == '.' }) {
                            amountStr = it
                            amountError = null
                        }
                    },
                    label = { Text("Valor (R$)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = amountError != null,
                    supportingText = { amountError?.let { Text(it) } },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_tx_amount_input")
                )

                // Category selector click trigger Dialog item
                Box(modifier = Modifier.fillMaxWidth()) {
                    var dropdownExpanded by remember { mutableStateOf(false) }
                    val matchingCategory = categories.firstOrNull { it.name == selectedCategory }

                    OutlinedButton(
                        onClick = { dropdownExpanded = true },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("add_tx_category_dropdown"),
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                matchingCategory?.let {
                                    Icon(
                                        imageVector = FinanceUtils.getIconByName(it.name),
                                        contentDescription = null,
                                        tint = FinanceUtils.parseColor(it.colorHex),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = selectedCategory,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .testTag("add_tx_category_menu")
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = FinanceUtils.getIconByName(category.name),
                                        contentDescription = null,
                                        tint = FinanceUtils.parseColor(category.colorHex)
                                    )
                                },
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category.name
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Optional Notes Input
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Observações (opcional)") },
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_tx_notes_input")
                )

                // Actions buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            var valid = true
                            if (title.isBlank()) {
                                titleError = "O nome é fundamental"
                                valid = false
                            }
                            val parsedAmount = amountStr.replace(",", ".").toDoubleOrNull()
                            if (parsedAmount == null || parsedAmount <= 0.0) {
                                amountError = "Digite um valor maior que 0"
                                valid = false
                            }

                            if (valid && parsedAmount != null) {
                                onConfirm(
                                    title.trim(),
                                    parsedAmount,
                                    System.currentTimeMillis(),
                                    selectedCategory,
                                    selectedType,
                                    notes.trim()
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.0f)
                            .testTag("add_tx_confirm_button")
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}
