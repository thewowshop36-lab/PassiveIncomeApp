package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.AdTask
import com.example.data.model.LiveNotification
import com.example.data.model.TeamMember
import com.example.data.model.TransactionItem
import com.example.data.model.UserAccount
import com.example.data.model.UserWallet
import com.example.data.repository.WalletRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AdPlaybackState(
    val isPlaying: Boolean = false,
    val currentAd: AdTask? = null,
    val remainingSeconds: Int = 10,
    val canClaimReward: Boolean = false,
    val isCompleted: Boolean = false
)

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WalletRepository

    val walletState: StateFlow<UserWallet?>
    val transactionsState: StateFlow<List<TransactionItem>>
    val teamMembersState: StateFlow<List<TeamMember>>
    val liveNotificationsState: StateFlow<List<LiveNotification>>

    private val _adPlayback = MutableStateFlow(AdPlaybackState())
    val adPlayback: StateFlow<AdPlaybackState> = _adPlayback.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _currentUserAccount = MutableStateFlow<UserAccount?>(null)
    val currentUserAccount: StateFlow<UserAccount?> = _currentUserAccount.asStateFlow()

    private var adTimerJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = WalletRepository(db.walletDao())

        walletState = repository.walletFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UserWallet()
        )

        transactionsState = repository.transactionsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        teamMembersState = repository.teamMembersFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        liveNotificationsState = repository.liveNotificationsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            _currentUserAccount.value = repository.getInitialOrLoggedInUser()
        }
    }

    fun getAvailableAds(): List<AdTask> = repository.getSampleAdTasks()

    fun startWatchingAd(ad: AdTask) {
        val currentWallet = walletState.value ?: UserWallet()
        if (currentWallet.todayAdsWatched >= currentWallet.dailyAdsLimit) {
            _userMessage.value = "Daily limit of ${currentWallet.dailyAdsLimit} ads reached! Upgrade VIP plan to watch more."
            return
        }

        adTimerJob?.cancel()
        _adPlayback.value = AdPlaybackState(
            isPlaying = true,
            currentAd = ad,
            remainingSeconds = ad.durationSeconds,
            canClaimReward = false,
            isCompleted = false
        )

        adTimerJob = viewModelScope.launch {
            for (sec in ad.durationSeconds downTo 1) {
                _adPlayback.value = _adPlayback.value.copy(remainingSeconds = sec)
                delay(1000)
            }
            _adPlayback.value = _adPlayback.value.copy(
                remainingSeconds = 0,
                canClaimReward = true,
                isCompleted = true
            )
        }
    }

    fun claimAdReward() {
        val state = _adPlayback.value
        val ad = state.currentAd ?: return
        if (!state.canClaimReward) return

        viewModelScope.launch {
            val result = repository.completeAdTask(ad)
            result.onSuccess { reward ->
                _userMessage.value = "🎉 Rewarded $${String.format("%.2f", reward)}! Credited to your wallet balance."
                _adPlayback.value = AdPlaybackState(isPlaying = false)
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Failed to claim reward."
                _adPlayback.value = AdPlaybackState(isPlaying = false)
            }
        }
    }

    fun cancelAdPlayback() {
        adTimerJob?.cancel()
        _adPlayback.value = AdPlaybackState(isPlaying = false)
        _userMessage.value = "Ad skipped. Rewards are only given after complete viewing."
    }

    fun requestWithdrawal(amount: Double, method: String, recipientAccount: String) {
        viewModelScope.launch {
            val result = repository.requestWithdrawal(amount, method, recipientAccount)
            result.onSuccess { ref ->
                _userMessage.value = "Withdrawal request submitted! Reference: $ref"
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Withdrawal failed."
            }
        }
    }

    fun depositFunds(amount: Double, method: String, referenceHash: String, screenshotUri: String = "") {
        viewModelScope.launch {
            val result = repository.depositFunds(amount, method, referenceHash, screenshotUri)
            result.onSuccess { ref ->
                _userMessage.value = "Deposit request for $${String.format("%.2f", amount)} submitted! Pending verification."
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Deposit request failed."
            }
        }
    }

    fun approveDeposit(transactionId: Long, amount: Double) {
        viewModelScope.launch {
            repository.approvePendingDeposit(transactionId, amount)
            _userMessage.value = "Approved deposit of $${String.format("%.2f", amount)} successfully!"
        }
    }

    fun upgradePlan(planId: Int) {
        viewModelScope.launch {
            val result = repository.upgradePlan(planId)
            result.onSuccess { planName ->
                _userMessage.value = "Congratulations! Upgraded to $planName."
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Upgrade failed."
            }
        }
    }

    fun registerUser(identifier: String, loginType: String, pass: String, name: String) {
        viewModelScope.launch {
            val res = repository.registerUser(identifier, loginType, pass, name)
            res.onSuccess { account ->
                _currentUserAccount.value = account
                _userMessage.value = "Welcome ${account.fullName}! $10 Signup Bonus Credited."
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Sign up failed."
            }
        }
    }

    fun loginUser(identifier: String, pass: String) {
        viewModelScope.launch {
            val res = repository.loginUser(identifier, pass)
            res.onSuccess { account ->
                _currentUserAccount.value = account
                _userMessage.value = "Welcome back, ${account.fullName}!"
            }.onFailure { err ->
                _userMessage.value = err.message ?: "Login failed."
            }
        }
    }

    fun logoutUser() {
        _currentUserAccount.value = null
        _userMessage.value = "Logged out successfully."
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
