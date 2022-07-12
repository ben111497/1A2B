package com.example.wordle.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface APIDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: API)

    @Query("SELECT * FROM $Table_API WHERE name LIKE :name AND page LIKE :page")
    fun getByNamePage(name: String, page: Int): API?

    @Query("DELETE FROM $Table_API WHERE name LIKE :name")
    fun deleteByName(name: String)
}