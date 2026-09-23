package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TeamMember
import com.example.data.model.UserWallet
import com.example.ui.theme.GoldenAccent
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.SuccessGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeamScreen(
    wallet: UserWallet,
    teamMembers: List<TeamMember>
) {
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Affiliate & Team Network",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Earn up to 3 Levels of direct referral and task commissions",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Referral Code Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Your Unique Referral Link & Code", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = wallet.referralCode,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = OrangePrimary
                        )

                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(wallet.referralCode))
                                copied = true
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = OrangePrimary)
                        }
                    }

                    if (copied) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Referral code copied to clipboard!", color = SuccessGreen, fontSize = 11.sp)
                    }
                }
            }
        }

        // 3-Level Commission Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Level 1
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Level 1 (10%)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = OrangePrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${wallet.teamCountLevel1} Members", fontSize = 11.sp)
                        Text("+$${String.format(Locale.US, "%.2f", wallet.teamEarningsLevel1)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                    }
                }

                // Level 2
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Level 2 (5%)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = GoldenAccent)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${wallet.teamCountLevel2} Members", fontSize = 11.sp)
                        Text("+$${String.format(Locale.US, "%.2f", wallet.teamEarningsLevel2)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                    }
                }

                // Level 3
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Level 3 (2%)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF42A5F5))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("${wallet.teamCountLevel3} Members", fontSize = 11.sp)
                        Text("+$${String.format(Locale.US, "%.2f", wallet.teamEarningsLevel3)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                    }
                }
            }
        }

        // Active Team Members List
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Your Direct Affiliates", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        items(teamMembers) { member ->
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
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = OrangePrimary)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(member.username, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Joined: ${dateFormat.format(Date(member.joinedTimestamp))} • Level ${member.level}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("+$${String.format(Locale.US, "%.2f", member.totalContributed)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuccessGreen)
                    Text("${member.adsCompletedToday} ads today", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
