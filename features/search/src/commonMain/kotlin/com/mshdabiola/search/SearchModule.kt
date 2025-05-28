package com.mshdabiola.search

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule =
    module {
        viewModelOf(::SearchViewModel)
    }
