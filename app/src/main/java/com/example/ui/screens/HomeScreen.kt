package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdTask
import com.example.data.model.LiveNotification
import com.example.data.model.TransactionItem
import com.example.data.model.UserWallet
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    wallet: UserWallet,
    transactions: List<TransactionItem>,
    liveTicker: List<LiveNotification>,
    adTasks: List<AdTask>,
    onWatchAd: (AdTask) -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onNavigatePlans: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Passive Income",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Verified Earnings & Daily Tasks",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = GoldenAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = wallet.vipTier,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Main Gradient Wallet Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(OrangeGradientStart, OrangeGradientEnd)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Balance",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "USD / USDT",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$${String.format(Locale.US, "%.2f", wallet.currentBalance)}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Earned", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                                Text("$${String.format(Locale.US, "%.2f", wallet.totalEarnings)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column {
                                Text("Withdrawn", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                                Text("$${String.format(Locale.US, "%.2f", wallet.totalWithdrawn)}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column {
                                Text("Today Ads", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                                Text("${wallet.todayAdsWatched}/${wallet.dailyAdsLimit}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Quick Deposit and Withdraw Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onOpenDeposit,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = null, tint = OrangePrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deposit", color = OrangeDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = onOpenWithdraw,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                            ) {
                                Icon(Icons.Default.ArrowOutward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Withdraw", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Live Ticker Marquee Strip
        if (liveTicker.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SuccessGreen.copy(alpha = 0.15f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = liveTicker.first().message,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Daily Ads Watch Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Rewarded Tasks",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Watch 10-sec sponsors & receive instant credits",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                TextButton(onClick = onNavigatePlans) {
                    Text("Upgrade Plan", fontSize = 12.sp, color = OrangePrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(adTasks) { ad ->
            val isLimitReached = wallet.todayAdsWatched >= wallet.dailyAdsLimit
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isLimitReached) { onWatchAd(ad) },
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = null,
                            tint = OrangePrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ad.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${ad.sponsorName} • ${ad.durationSeconds}s duration",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isLimitReached) Color.Gray.copy(alpha = 0.2f) else SuccessGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (isLimitReached) "Limit Done" else "+$${String.format(Locale.US, "%.2f", ad.rewardAmount)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isLimitReached) Color.Gray else SuccessGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Recent Transaction History Section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recent Transactions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (transactions.isEmpty()) {
            item {
                Text(
                    text = "No transactions yet. Complete ad tasks or deposit funds.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(transactions.take(5)) { txn ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (txn.isCredit) SuccessGreen.copy(alpha = 0.15f) else ErrorRed.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (txn.isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = if (txn.isCredit) SuccessGreen else ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = txn.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${txn.method} • ${dateFormat.format(Date(txn.timestamp))}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "${if (txn.isCredit) "+" else "-"}$${String.format(Locale.US, "%.2f", txn.amount)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (txn.isCredit) SuccessGreen else ErrorRed
                    )
                }
            }
        }
    }
}
