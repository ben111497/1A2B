package com.example.wordle.fragment.repository

import android.content.Context
import android.util.Log
import com.example.wordle.database.API
import com.example.wordle.database.DataBase
import com.example.wordle.api.AttractionsClassInterface.Companion.service
import com.example.wordle.api.AttractionsClassRes
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class BlankRepository constructor(context: Context) {
    private val dataBase: DataBase by lazy { DataBase.instance(context) }

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
     * Web
     */

    interface APIResponse {
        fun response(data: AttractionsClassRes?)
    }

    suspend fun getResponse(language: String, page: Int, listener: APIResponse) {
        withContext(Dispatchers.Main) {
            service.get(language, page).enqueue(object: Callback<AttractionsClassRes> {
                override fun onResponse(call: Call<AttractionsClassRes>, response: Response<AttractionsClassRes>) {
                    if (response.isSuccessful) {
                        Log.e("API: AttractionsClassRes", response.body().toString())
                        saveAPIByName(AttractionsClassRes::class.java, page, Gson().toJson(response.body()))
                        listener.response(response.body())
                    } else {
                        Log.e("API: AttractionsClassRes", response.errorBody()?.charStream()?.readText().toString())
                        throw Exception(response.errorBody()?.charStream()?.readText())
                    }
                }

                override fun onFailure(call: Call<AttractionsClassRes>, t: Throwable) {
                    Log.e("getAttractionsClass", t.message ?: "error")
                    throw Exception(t.message)
                }
            })
        }
    }
}