package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_wallet")
data class UserWallet(
    @PrimaryKey val id: Int = 1,
    val currentBalance: Double = 12.50, // Initial balance
    val totalEarnings: Double = 48.75,
    val totalWithdrawn: Double = 35.00,
    val pendingWithdrawal: Double = 0.00,
    val todayAdsWatched: Int = 3,
    val dailyAdsLimit: Int = 10,
    val referralCode: String = "PI-984251",
    val teamCountLevel1: Int = 8,
    val teamCountLevel2: Int = 14,
    val teamCountLevel3: Int = 29,
    val teamEarningsLevel1: Double = 16.40,
    val teamEarningsLevel2: Double = 11.20,
    val teamEarningsLevel3: Double = 8.70,
    val lastRewardClaimTimestamp: Long = 0L,
    val activePlanId: Int = 3, // Defaults to VIP 3
    val accountNumber: String = "PK-8842-9910",
    val channelNumber: String = "CH-07",
    val vipTier: String = "VIP 3 Gold"
)

@Entity(tableName = "transactions")
data class TransactionItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val amount: Double,
    val isCredit: Boolean,
    val timestamp: Long,
    val method: String,
    val status: String = "Completed",
    val referenceHash: String = "",
    val screenshotUri: String = ""
)

@Entity(tableName = "team_members")
data class TeamMember(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val level: Int,
    val joinedTimestamp: Long,
    val totalContributed: Double,
    val adsCompletedToday: Int
)

@Entity(tableName = "live_notifications")
data class LiveNotification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val message: String,
    val timestamp: Long,
    val type: String
)

data class AdTask(
    val id: String,
    val title: String,
    val sponsorName: String,
    val rewardAmount: Double = 0.05, // Fixed $0.05 reward
    val durationSeconds: Int = 10,
    val category: String,
    val videoAssetDescription: String
)

data class InvestmentPlan(
    val planId: Int,
    val name: String,
    val badge: String,
    val depositPriceUsd: Double,
    val dailyAdsLimit: Int,
    val dailyEarningUsd: Double,
    val monthlyRoiPercent: Int,
    val validityDays: Int,
    val colorHex: Long
)

// 10 Standard Structured VIP Investment Plans
val STANDARD_INVESTMENT_PLANS = listOf(
    InvestmentPlan(1, "Plan 1 - Bronze Starter", "VIP 1", 20.0, 5, 1.00, 150, 60, 0xFFCD7F32),
    InvestmentPlan(2, "Plan 2 - Silver Classic", "VIP 2", 50.0, 8, 2.50, 150, 60, 0xFF9E9E9E),
    InvestmentPlan(3, "Plan 3 - Gold Advance", "VIP 3", 100.0, 12, 5.00, 150, 60, 0xFFFFB300),
    InvestmentPlan(4, "Plan 4 - Platinum Pro", "VIP 4", 200.0, 18, 11.00, 165, 60, 0xFF42A5F5),
    InvestmentPlan(5, "Plan 5 - Diamond Executive", "VIP 5", 500.0, 25, 30.00, 180, 60, 0xFF26C6DA),
    InvestmentPlan(6, "Plan 6 - Crown Royal", "VIP 6", 1000.0, 35, 65.00, 195, 60, 0xFFAB47BC),
    InvestmentPlan(7, "Plan 7 - Ruby Master", "VIP 7", 2000.0, 50, 140.00, 210, 60, 0xFFE91E63),
    InvestmentPlan(8, "Plan 8 - Sapphire Prestige", "VIP 8", 3500.0, 70, 260.00, 225, 60, 0xFF3F51B5),
    InvestmentPlan(9, "Plan 9 - Emerald Elite", "VIP 9", 5000.0, 90, 400.00, 240, 60, 0xFF00897B),
    InvestmentPlan(10, "Plan 10 - Imperial Titan", "VIP 10", 10000.0, 120, 850.00, 255, 60, 0xFFFF6D00)
)
