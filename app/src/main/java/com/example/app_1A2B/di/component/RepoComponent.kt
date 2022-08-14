package com.example.app_1A2B.di.component

import com.example.app_1A2B.di.module.LocalDataModule
import com.example.app_1A2B.ui.component.fragment.homepage.HomePageFragment
import dagger.Component

@Component(modules = [LocalDataModule::class])
interface LocalDataComponent {
    fun inject(fragment: HomePageFragment)
}