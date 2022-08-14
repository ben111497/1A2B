package com.example.app_1A2B.di.module

import android.content.Context
import com.example.app_1A2B.data.local.LocalData
import dagger.Module
import dagger.Provides

@Module
class LocalDataModule(private val context: Context)  {
    @Provides
    fun provideLocal(): LocalData = LocalData(context)
}