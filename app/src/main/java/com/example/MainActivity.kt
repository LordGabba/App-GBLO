package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FinanceViewModel
import com.example.ui.viewmodel.FinanceViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application = application as FinanceApplication
        val viewModel: FinanceViewModel by viewModels {
            FinanceViewModelFactory(application.repository)
        }

        setContent {
            val profile by viewModel.profile.collectAsState()

            MyApplicationTheme(darkTheme = profile.useDarkTheme) {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : Screen(
        route = "dashboard",
        title = "Início",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object Transactions : Screen(
        route = "transactions",
        title = "Lançamentos",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet
    )
    object Categories : Screen(
        route = "categories",
        title = "Categorias",
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category
    )
    object Reports : Screen(
        route = "reports",
        title = "Relatórios",
        selectedIcon = Icons.Filled.Assessment,
        unselectedIcon = Icons.Outlined.Assessment
    )
    object Settings : Screen(
        route = "settings",
        title = "Ajustes",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
}

@Composable
fun MainAppContainer(viewModel: FinanceViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

    val navigationItems = listOf(
        Screen.Dashboard,
        Screen.Transactions,
        Screen.Categories,
        Screen.Reports,
        Screen.Settings
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_bottom_nav"),
                tonalElevation = 8.dp
            ) {
                navigationItems.forEach { screen ->
                    val isSelected = currentScreen == screen
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        modifier = Modifier.testTag("nav_item_${screen.route}"),
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()) // Handle bottom bar collision gracefully
        ) {
            when (currentScreen) {
                Screen.Dashboard -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToTransactions = { currentScreen = Screen.Transactions },
                        onNavigateToReports = { currentScreen = Screen.Reports }
                    )
                }
                Screen.Transactions -> {
                    TransactionsScreen(viewModel = viewModel)
                }
                Screen.Categories -> {
                    CategoriesScreen(viewModel = viewModel)
                }
                Screen.Reports -> {
                    ReportsScreen(viewModel = viewModel)
                }
                Screen.Settings -> {
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }
}
