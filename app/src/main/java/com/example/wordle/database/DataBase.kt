package com.example.wordle.database

import android.content.Context
import androidx.room.*

const val Table_API = "API"
const val DB_Version = 2

@Database(entities = [API::class], version = DB_Version)

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
}

@Entity(tableName = Table_API, primaryKeys = ["name", "page"])
data class API (var name: String, var page: Int, var json: String)
