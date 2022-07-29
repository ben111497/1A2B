package com.example.wordle_1A2B.data.local.database

import android.content.Context
import androidx.room.*

const val Table_API = "API"
const val Table_Coin = "Coin"
const val DB_Version = 3

@Database(entities = [API::class, Coin::class], version = DB_Version)
@TypeConverters(Converters::class)
abstract class DataBase: RoomDatabase() {
    companion object {
        private var instance: DataBase? = null

        fun instance(context: Context): DataBase {
            return instance ?:
                Room.databaseBuilder(context, DataBase::class.java, "wordle")
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
        }
    }

    abstract fun getAPIDao(): APIDao
    abstract fun getCoinDao(): CoinDao
}

@Entity(tableName = Table_API, primaryKeys = ["name"])
data class API (var name: String, var json: String)

@Entity(tableName = Table_Coin, primaryKeys = ["userID"])
data class Coin (var userID: String, var coin: Int)