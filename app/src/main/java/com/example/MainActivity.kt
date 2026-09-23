package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserWallet
import com.example.ui.components.AuthDialog
import com.example.ui.components.DepositBottomSheet
import com.example.ui.components.VideoAdPlayerDialog
import com.example.ui.components.WithdrawalBottomSheet
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrangePrimary
import com.example.ui.viewmodel.WalletViewModel

enum class ScreenTab(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    PLANS("VIP Plans", Icons.Default.WorkspacePremium),
    TEAM("Team", Icons.Default.Group),
    ACCOUNT("Account", Icons.Default.AccountCircle)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: WalletViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(ScreenTab.HOME) }
    var showDepositSheet by remember { mutableStateOf(false) }
    var showWithdrawSheet by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    val wallet by viewModel.walletState.collectAsStateWithLifecycle()
    val transactions by viewModel.transactionsState.collectAsStateWithLifecycle()
    val teamMembers by viewModel.teamMembersState.collectAsStateWithLifecycle()
    val liveTicker by viewModel.liveNotificationsState.collectAsStateWithLifecycle()
    val adPlayback by viewModel.adPlayback.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val userAccount by viewModel.currentUserAccount.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    val activeWallet = wallet ?: UserWallet()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                ScreenTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                tint = if (isSelected) OrangePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                color = if (isSelected) OrangePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentTab) {
                ScreenTab.HOME -> {
                    HomeScreen(
                        wallet = activeWallet,
                        transactions = transactions,
                        liveTicker = liveTicker,
                        adTasks = viewModel.getAvailableAds(),
                        onWatchAd = { ad -> viewModel.startWatchingAd(ad) },
                        onOpenDeposit = { showDepositSheet = true },
                        onOpenWithdraw = { showWithdrawSheet = true },
                        onNavigatePlans = { currentTab = ScreenTab.PLANS }
                    )
                }
                ScreenTab.PLANS -> {
                    PlansScreen(
                        wallet = activeWallet,
                        onUpgradePlan = { planId -> viewModel.upgradePlan(planId) },
                        onOpenDeposit = { showDepositSheet = true }
                    )
                }
                ScreenTab.TEAM -> {
                    TeamScreen(
                        wallet = activeWallet,
                        teamMembers = teamMembers
                    )
                }
                ScreenTab.ACCOUNT -> {
                    AccountScreen(
                        wallet = activeWallet,
                        userAccount = userAccount,
                        onOpenAuthDialog = { showAuthDialog = true },
                        onLogout = { viewModel.logoutUser() },
                        onOpenDeposit = { showDepositSheet = true },
                        onOpenWithdraw = { showWithdrawSheet = true }
                    )
                }
            }
        }

        // Modals & Sheets
        if (adPlayback.isPlaying) {
            VideoAdPlayerDialog(
                playbackState = adPlayback,
                onClaimReward = { viewModel.claimAdReward() },
                onClose = { viewModel.cancelAdPlayback() }
            )
        }

        if (showDepositSheet) {
            DepositBottomSheet(
                onDismiss = { showDepositSheet = false },
                onSubmitDeposit = { amt, method, ref, slip ->
                    viewModel.depositFunds(amt, method, ref, slip)
                }
            )
        }

        if (showWithdrawSheet) {
            WithdrawalBottomSheet(
                availableBalance = activeWallet.currentBalance,
                onDismiss = { showWithdrawSheet = false },
                onSubmitWithdrawal = { amt, method, acc ->
                    viewModel.requestWithdrawal(amt, method, acc)
                }
            )
        }

        if (showAuthDialog) {
            AuthDialog(
                onDismiss = { showAuthDialog = false },
                onLogin = { id, pass -> viewModel.loginUser(id, pass) },
                onRegister = { id, type, pass, name -> viewModel.registerUser(id, type, pass, name) }
            )
        }
    }
}
