package com.mshdabiola.wikipediacommons.di

import com.mshdabiola.data.di.dataModule
import com.mshdabiola.detail.detailModule
import com.mshdabiola.main.mainModule
import com.mshdabiola.search.searchModule
import com.mshdabiola.setting.settingModule
import com.mshdabiola.wikipediacommons.MainAppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        includes(dataModule, detailModule, mainModule, settingModule, searchModule)
        viewModel { MainAppViewModel(get()) }
    }
