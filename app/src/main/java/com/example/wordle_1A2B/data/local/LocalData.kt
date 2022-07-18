package com.example.wordle_1A2B.data.local

import android.content.Context
import android.util.Log
import com.example.wordle_1A2B.data.local.database.API
import com.example.wordle_1A2B.data.local.database.DataBase
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

open class LocalData constructor(private val context: Context) {
    private val dataBase: DataBase get() { return DataBase.instance(context) }

    /**
     * local
     */

    interface Local {
        fun <T> onGetLocalData(data: T)
    }

    fun getLocalAPIByName(resOfT: Class<*>, page: Int = 1, listener: Local) {
        runBlocking {
            var data: API? = null
            val job = GlobalScope.launch(Dispatchers.IO) {
                try {
                    data = dataBase.getAPIDao().getByNamePage(resOfT.simpleName, page) ?: kotlin.run {
                        Log.e("Local not find: ${resOfT.simpleName}", resOfT.simpleName)
                        return@launch
                    }
                } catch (e: Exception) {
                    Log.e("Local get failed: ${resOfT.simpleName}", e.message ?: "")
                }
            }

            job.join()

            data?.let {
                val json = Gson().fromJson(it.json, resOfT)
                Log.e("Local: ${resOfT.simpleName}", json.toString())
                listener.onGetLocalData(json)
            }
        }
    }

    fun saveAPIByName(resOfT: Class<*>, page: Int = 1, json: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                dataBase.getAPIDao().insert(API(resOfT.simpleName, page, json))
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