package com.example.app_1A2B.data.local

import android.content.Context
import com.example.app_1A2B.data.local.database.API
import com.example.app_1A2B.data.local.database.Coin
import com.example.app_1A2B.data.local.database.DataBase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

@DelicateCoroutinesApi
open class LocalData constructor(private val context: Context) {
    private val dataBase: DataBase get() { return DataBase.instance(context) }

    fun getAPIByName(resOfT: Class<*>): Flow<API> {
        return try {
            dataBase.getAPIDao().getByNamePage(resOfT.simpleName) ?: throw IllegalArgumentException("UnFind")
        } catch (e: Exception) {
            throw IllegalArgumentException("UnFind")
        }
    }

    fun saveAPIByName(resOfT: Class<*>, json: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                dataBase.getAPIDao().insert(API(resOfT.simpleName, json))
            } catch (e: Exception) { }
        }
    }

    fun getCoin(): Flow<Coin?> {
        return try {
            dataBase.getCoinDao().getByUserID("")
        } catch (e: Exception) {
            setCoin("", 0)
            throw IllegalArgumentException("UnFind")
        }
    }

    fun setCoin(userID: String = "", coin: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                dataBase.getCoinDao().insert(Coin(userID, coin))
            } catch (e: Exception) { }
        }
    }

    /**
     * Web 之後再刪掉，當作參考用
     */

//    interface APIResponse {
//        fun response(data: AttractionsClassRes?)
//    }
//
//    suspend fun getResponse(language: String, page: Int, listener: APIResponse) {
//        withContext(Dispatchers.Main) {
//            AttractionsClassInterface.service.get(language, page).enqueue(object: Callback<AttractionsClassRes> {
//                override fun onResponse(call: Call<AttractionsClassRes>, response: Response<AttractionsClassRes>) {
//                    if (response.isSuccessful) {
//                        Log.e("API: AttractionsClassRes", response.body().toString())
//                        saveAPIByName(AttractionsClassRes::class.java, page, Gson().toJson(response.body()))
//                        listener.response(response.body())
//                    } else {
//                        Log.e("API: AttractionsClassRes", response.errorBody()?.charStream()?.readText().toString())
//                        throw Exception(response.errorBody()?.charStream()?.readText())
//                    }
//                }
//
//                override fun onFailure(call: Call<AttractionsClassRes>, t: Throwable) {
//                    Log.e("getAttractionsClass", t.message ?: "error")
//                    throw Exception(t.message)
//                }
//            })
//        }
//    }
}