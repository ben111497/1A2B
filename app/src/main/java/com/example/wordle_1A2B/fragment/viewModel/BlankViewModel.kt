package com.example.wordle_1A2B.fragment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.fragment.repository.BlankRepository
import com.example.wordle_1A2B.api.AttractionsClassRes
import com.example.wordle_1A2B.utils.addAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//ViewModel
//定義一個 viewModel，用來跟 Repository 溝通取得資料，一般會配合 LiveData 使用
class BlankViewModel(private val blankRepository: BlankRepository): ViewModel() {
    val page = MutableLiveData<Int>().also { it.value = 1 }
    val attractionsList = MutableLiveData<ArrayList<AttractionsClassRes.Data>>().also { it.value = ArrayList<AttractionsClassRes.Data>() }
    val attractionsListAll = MutableLiveData<ArrayList<AttractionsClassRes.Data>>().also { it.value = ArrayList<AttractionsClassRes.Data>() }

    var total: Int = 1
    var selectIndex: Int = 0

    fun getPage() = page.value!!

    fun changePage(number: Int) {
        if (number < 1) return
        page.value = number
        getAttractionClassReq(number)
        getLocalAPIByName(AttractionsClassRes::class.java, number)
    }

    fun getAttractionsList() = attractionsList.value

    fun getListCategory(): String {
        var data = ""
        getAttractionsList()?.get(selectIndex)?.category?.let { for (i in it) { data += "${i.name}、" } }
        getAttractionsList()?.get(selectIndex)?.target?.let { for (i in it) { data += "${i.name}、" } }
        return data.subSequence(0, data.length - 1) as String
    }

    private fun getAttractionClassReq(page: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            blankRepository.getResponse("zh-tw", page, object: BlankRepository.APIResponse{
                override fun response(data: AttractionsClassRes?) {
                    if (page != this@BlankViewModel.page.value || data == null) return

                    total = data.total ?: 0
                    data.data?.let {
                        attractionsList.value?.clear()
                        attractionsList.addAll(it)
                    }
                    data.data?.let { if (page * 10 > attractionsListAll.value?.size ?: 0) attractionsListAll.addAll(it) }
                }
            })
        }
    }

    private fun getLocalAPIByName(resOfT: Class<*>, page: Int = 1) {
        blankRepository.getLocalAPIByName(resOfT, page, object: BlankRepository.Local {
            override fun <T> onGetLocalData(data: T) {
                when (data) {
                    is AttractionsClassRes -> {
                        if (page != this@BlankViewModel.page.value) return

                        total = data.total ?: 0
                        data.data?.let {
                            attractionsList.value?.clear()
                            attractionsList.addAll(it)
                        }
                        data.data?.let { if (page * 10 > attractionsListAll.value?.size ?: 0) attractionsListAll.addAll(it) }
                    }
                }
            }
        })
    }
}