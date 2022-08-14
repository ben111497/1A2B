package com.example.app_1A2B.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface APIDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: API)

    @Query("SELECT * FROM $Table_API WHERE name LIKE :name")
    fun getByNamePage(name: String): Flow<API>?

    @Query("DELETE FROM $Table_API WHERE name LIKE :name")
    suspend fun deleteByName(name: String): Int
}

@Dao
interface CoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Coin)

    @Query("SELECT * FROM $Table_Coin WHERE userID LIKE :userID")
    fun getByUserID(userID: String): Flow<Coin?>

    @Query("DELETE FROM $Table_Coin WHERE userID LIKE :userID")
    suspend fun delete(userID: String): Int
}