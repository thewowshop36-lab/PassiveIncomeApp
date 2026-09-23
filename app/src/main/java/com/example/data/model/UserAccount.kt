package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val identifier: String, // Email or Mobile number
    val loginType: String,   // "EMAIL" or "PHONE"
    val passwordHash: String,
    val fullName: String,
    val registeredTimestamp: Long = System.currentTimeMillis()
)
