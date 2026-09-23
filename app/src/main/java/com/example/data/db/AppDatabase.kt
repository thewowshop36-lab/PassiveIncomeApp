package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.LiveNotification
import com.example.data.model.TeamMember
import com.example.data.model.TransactionItem
import com.example.data.model.UserAccount
import com.example.data.model.UserWallet

@Database(
    entities = [
        UserWallet::class,
        TransactionItem::class,
        TeamMember::class,
        LiveNotification::class,
        UserAccount::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun walletDao(): WalletDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "passive_income_wallet.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
