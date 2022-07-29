package com.example.wordle_1A2B.utils

import android.app.Activity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> MutableLiveData<ArrayList<T>>.add(liveData: T) {
    val value = this.value ?: ArrayList<T>()
    value.add(liveData)
    this.value = value
}

fun <T> MutableLiveData<ArrayList<T>>.addAll(liveData: ArrayList<T>) {
    val value = this.value ?: ArrayList<T>()
    value.addAll(liveData)
    this.value = value
}

fun <T> MutableLiveData<ArrayList<T>>.remove(liveData: T) {
    val value = this.value ?: ArrayList<T>()
    value.remove(liveData)
    this.value = value
}

fun <T> MutableLiveData<ArrayList<T>>.clear() {
    this.value = ArrayList<T>()
}