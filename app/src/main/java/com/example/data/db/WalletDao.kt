package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LiveNotification
import com.example.data.model.TeamMember
import com.example.data.model.TransactionItem
import com.example.data.model.UserAccount
import com.example.data.model.UserWallet
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDao {

    @Query("SELECT * FROM user_wallet WHERE id = 1 LIMIT 1")
    fun getWallet(): Flow<UserWallet?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: UserWallet)

    @Update
    suspend fun updateWallet(wallet: UserWallet)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getTransactions(): Flow<List<TransactionItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(item: TransactionItem)

    @Query("SELECT * FROM team_members ORDER BY joinedTimestamp DESC")
    fun getTeamMembers(): Flow<List<TeamMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMember(member: TeamMember)

    @Query("SELECT * FROM live_notifications ORDER BY timestamp DESC LIMIT 25")
    fun getLiveNotifications(): Flow<List<LiveNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveNotification(notif: LiveNotification)

    // User Authentication DAO queries
    @Query("SELECT * FROM user_accounts WHERE identifier = :identifier LIMIT 1")
    suspend fun getUserByPhoneOrEmail(identifier: String): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserAccount?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(account: UserAccount): Long

    @Query("SELECT * FROM user_accounts ORDER BY id DESC LIMIT 1")
    suspend fun getLatestUser(): UserAccount?
}
