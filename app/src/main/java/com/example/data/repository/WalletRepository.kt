package com.example.data.repository

import com.example.data.db.WalletDao
import com.example.data.model.AdTask
import com.example.data.model.LiveNotification
import com.example.data.model.STANDARD_INVESTMENT_PLANS
import com.example.data.model.TeamMember
import com.example.data.model.TransactionItem
import com.example.data.model.UserAccount
import com.example.data.model.UserWallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.random.Random

class WalletRepository(private val dao: WalletDao) {

    val walletFlow: Flow<UserWallet?> = dao.getWallet()
    val transactionsFlow: Flow<List<TransactionItem>> = dao.getTransactions()
    val teamMembersFlow: Flow<List<TeamMember>> = dao.getTeamMembers()
    val liveNotificationsFlow: Flow<List<LiveNotification>> = dao.getLiveNotifications()

    private val twentyFourHoursMillis = 24L * 60L * 60L * 1000L

    suspend fun checkAndSeedInitialData() {
        val currentWallet = dao.getWallet().firstOrNull()
        if (currentWallet == null) {
            val initial = UserWallet()
            dao.insertWallet(initial)

            // Seed initial realistic transactions
            val now = System.currentTimeMillis()
            val initialTransactions = listOf(
                TransactionItem(
                    title = "Daily Video Ad Rewards",
                    amount = 0.15,
                    isCredit = true,
                    timestamp = now - 3600000 * 2,
                    method = "Rewarded Ads"
                ),
                TransactionItem(
                    title = "Team Level 1 Commission",
                    amount = 2.40,
                    isCredit = true,
                    timestamp = now - 3600000 * 6,
                    method = "Affiliate Bonus"
                ),
                TransactionItem(
                    title = "Withdrawal to Binance (USDT)",
                    amount = 25.00,
                    isCredit = false,
                    timestamp = now - 86400000 * 2,
                    method = "USDT (TRC-20)"
                ),
                TransactionItem(
                    title = "Deposit via JazzCash",
                    amount = 50.00,
                    isCredit = true,
                    timestamp = now - 86400000 * 5,
                    method = "JazzCash"
                )
            )
            initialTransactions.forEach { dao.insertTransaction(it) }

            // Seed realistic team members
            val team = listOf(
                TeamMember(username = "farhan_investor", level = 1, joinedTimestamp = now - 86400000 * 4, totalContributed = 12.80, adsCompletedToday = 10),
                TeamMember(username = "ali_crypto99", level = 1, joinedTimestamp = now - 86400000 * 7, totalContributed = 24.50, adsCompletedToday = 8),
                TeamMember(username = "hamza_trader", level = 2, joinedTimestamp = now - 86400000 * 10, totalContributed = 6.40, adsCompletedToday = 5),
                TeamMember(username = "usman_pro", level = 2, joinedTimestamp = now - 86400000 * 12, totalContributed = 18.00, adsCompletedToday = 9),
                TeamMember(username = "sana_tech", level = 3, joinedTimestamp = now - 86400000 * 15, totalContributed = 4.20, adsCompletedToday = 6)
            )
            team.forEach { dao.insertTeamMember(it) }

            // Seed live ticker notifications
            val liveTicker = listOf(
                LiveNotification(message = "user***891 withdrew $45.00 via USDT", timestamp = now - 60000, type = "Withdrawal"),
                LiveNotification(message = "user***204 deposited $100.00 via JazzCash", timestamp = now - 180000, type = "Deposit"),
                LiveNotification(message = "user***772 earned $5.00 daily ROI from VIP 3", timestamp = now - 300000, type = "Reward"),
                LiveNotification(message = "user***419 completed 12/12 Daily Ad Tasks", timestamp = now - 420000, type = "Task")
            )
            liveTicker.forEach { dao.insertLiveNotification(it) }
        }
    }

    suspend fun completeAdTask(ad: AdTask): Result<Double> {
        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()
        if (currentWallet.todayAdsWatched >= currentWallet.dailyAdsLimit) {
            return Result.failure(Exception("Daily ad viewing limit reached for today! Check back tomorrow or upgrade plan."))
        }

        val updatedBalance = currentWallet.currentBalance + ad.rewardAmount
        val updatedTotalEarnings = currentWallet.totalEarnings + ad.rewardAmount
        val updatedWatchedCount = currentWallet.todayAdsWatched + 1

        val updatedWallet = currentWallet.copy(
            currentBalance = (updatedBalance * 100).toLong() / 100.0,
            totalEarnings = (updatedTotalEarnings * 100).toLong() / 100.0,
            todayAdsWatched = updatedWatchedCount
        )

        dao.updateWallet(updatedWallet)

        dao.insertTransaction(
            TransactionItem(
                title = "Watched: ${ad.title}",
                amount = ad.rewardAmount,
                isCredit = true,
                timestamp = System.currentTimeMillis(),
                method = "Video Ad Reward"
            )
        )

        dao.insertLiveNotification(
            LiveNotification(
                message = "You earned $${String.format("%.2f", ad.rewardAmount)} for watching ad!",
                timestamp = System.currentTimeMillis(),
                type = "Earning"
            )
        )

        return Result.success(ad.rewardAmount)
    }

    suspend fun requestWithdrawal(amount: Double, method: String, recipientAccount: String): Result<String> {
        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()

        if (amount < 5.0) {
            return Result.failure(Exception("Minimum withdrawal threshold is $5.00 USD."))
        }

        if (amount > currentWallet.currentBalance) {
            return Result.failure(Exception("Insufficient wallet balance. Available: $${String.format("%.2f", currentWallet.currentBalance)}"))
        }

        val updatedBalance = currentWallet.currentBalance - amount
        val updatedWithdrawn = currentWallet.totalWithdrawn + amount

        dao.updateWallet(
            currentWallet.copy(
                currentBalance = (updatedBalance * 100).toLong() / 100.0,
                totalWithdrawn = (updatedWithdrawn * 100).toLong() / 100.0
            )
        )

        val txId = "TXN-${Random.nextInt(100000, 999999)}"
        dao.insertTransaction(
            TransactionItem(
                title = "Withdrawal to $recipientAccount",
                amount = amount,
                isCredit = false,
                timestamp = System.currentTimeMillis(),
                method = method
            )
        )

        dao.insertLiveNotification(
            LiveNotification(
                message = "Withdrawal of $${String.format("%.2f", amount)} ($method) submitted successfully! Ref: $txId",
                timestamp = System.currentTimeMillis(),
                type = "Withdrawal"
            )
        )

        return Result.success(txId)
    }

    suspend fun depositFunds(amount: Double, method: String, referenceHash: String = "", screenshotUri: String = ""): Result<String> {
        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()

        if (amount <= 0.0) {
            return Result.failure(Exception("Please enter a valid deposit amount."))
        }

        val refId = if (referenceHash.isNotBlank()) referenceHash else "DEP-${Random.nextInt(100000, 999999)}"

        dao.insertTransaction(
            TransactionItem(
                title = "Deposit via $method (Pending)",
                amount = amount,
                isCredit = true,
                timestamp = System.currentTimeMillis(),
                method = method,
                status = "Pending Approval",
                referenceHash = refId,
                screenshotUri = screenshotUri
            )
        )

        dao.insertLiveNotification(
            LiveNotification(
                message = "Deposit request for $${String.format("%.2f", amount)} submitted for verification.",
                timestamp = System.currentTimeMillis(),
                type = "Deposit"
            )
        )

        return Result.success(refId)
    }

    suspend fun approvePendingDeposit(transactionId: Long, creditedAmount: Double): Result<Unit> {
        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()
        val updatedWallet = currentWallet.copy(
            currentBalance = currentWallet.currentBalance + creditedAmount,
            totalEarnings = currentWallet.totalEarnings + creditedAmount
        )
        dao.updateWallet(updatedWallet)

        dao.insertLiveNotification(
            LiveNotification(
                message = "✅ Admin approved deposit of $${String.format("%.2f", creditedAmount)}! Balance updated.",
                timestamp = System.currentTimeMillis(),
                type = "DepositApproved"
            )
        )
        return Result.success(Unit)
    }

    suspend fun upgradePlan(planId: Int): Result<String> {
        val plan = STANDARD_INVESTMENT_PLANS.firstOrNull { it.planId == planId }
            ?: return Result.failure(Exception("Invalid plan selected."))

        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()

        if (currentWallet.currentBalance < plan.depositPriceUsd) {
            return Result.failure(
                Exception("Insufficient balance ($${String.format("%.2f", currentWallet.currentBalance)}). You need $${String.format("%.2f", plan.depositPriceUsd)} to activate ${plan.name}.")
            )
        }

        val newBalance = currentWallet.currentBalance - plan.depositPriceUsd
        val updatedWallet = currentWallet.copy(
            currentBalance = (newBalance * 100).toLong() / 100.0,
            activePlanId = plan.planId,
            vipTier = plan.badge,
            dailyAdsLimit = plan.dailyAdsLimit
        )
        dao.updateWallet(updatedWallet)

        dao.insertTransaction(
            TransactionItem(
                title = "Activated ${plan.name}",
                amount = plan.depositPriceUsd,
                isCredit = false,
                timestamp = System.currentTimeMillis(),
                method = "Plan Upgrade"
            )
        )

        dao.insertLiveNotification(
            LiveNotification(
                message = "🎉 Upgraded to ${plan.badge}! Daily task limit is now ${plan.dailyAdsLimit}.",
                timestamp = System.currentTimeMillis(),
                type = "Upgrade"
            )
        )

        return Result.success(plan.name)
    }

    suspend fun registerUser(identifier: String, loginType: String, passwordPlain: String, fullName: String): Result<UserAccount> {
        val cleanIdentifier = identifier.trim().lowercase()
        if (cleanIdentifier.isBlank() || passwordPlain.length < 4) {
            return Result.failure(Exception("Valid identifier and minimum 4-char password required."))
        }
        val existing = dao.getUserByPhoneOrEmail(cleanIdentifier)
        if (existing != null) {
            return Result.failure(Exception("User with this $loginType already exists! Please log in."))
        }

        val newAccount = UserAccount(
            identifier = cleanIdentifier,
            loginType = loginType,
            passwordHash = passwordPlain,
            fullName = if (fullName.isNotBlank()) fullName else "Member ${Random.nextInt(100, 999)}"
        )
        val insertedId = dao.registerUser(newAccount)
        val created = dao.getUserById(insertedId) ?: newAccount

        // Credit $10 welcome bonus
        val currentWallet = dao.getWallet().firstOrNull() ?: UserWallet()
        dao.updateWallet(
            currentWallet.copy(
                currentBalance = currentWallet.currentBalance + 10.0,
                totalEarnings = currentWallet.totalEarnings + 10.0
            )
        )
        dao.insertTransaction(
            TransactionItem(
                title = "Welcome Signup Bonus",
                amount = 10.00,
                isCredit = true,
                timestamp = System.currentTimeMillis(),
                method = "Bonus Credit"
            )
        )

        return Result.success(created)
    }

    suspend fun loginUser(identifier: String, passwordPlain: String): Result<UserAccount> {
        val cleanIdentifier = identifier.trim().lowercase()
        val existing = dao.getUserByPhoneOrEmail(cleanIdentifier)
            ?: return Result.failure(Exception("Account not found for $identifier. Please sign up."))

        if (existing.passwordHash != passwordPlain) {
            return Result.failure(Exception("Incorrect password. Please try again."))
        }
        return Result.success(existing)
    }

    suspend fun getInitialOrLoggedInUser(): UserAccount? {
        return dao.getLatestUser()
    }

    fun getSampleAdTasks(): List<AdTask> {
        return listOf(
            AdTask("ad_1", "Crypto Arbitrage Bot Tutorial", "Binance Global", 0.05, 10, "Crypto", "Learn algorithmic trading strategies"),
            AdTask("ad_2", "NextGen Solar Energy Solutions", "EcoWatt Corp", 0.05, 10, "Green Tech", "Residential clean power installation"),
            AdTask("ad_3", "Automated Forex Copy Trading", "OctaTrade", 0.05, 10, "Forex", "Mirror verified top traders daily"),
            AdTask("ad_4", "Global E-Commerce Logistics Hub", "DropFast Global", 0.05, 10, "Commerce", "Fulfillment and cross-border shipping"),
            AdTask("ad_5", "AI Marketing Automation Suite", "AgentScale AI", 0.05, 10, "AI & Cloud", "Generate social campaigns in minutes")
        )
    }
}
