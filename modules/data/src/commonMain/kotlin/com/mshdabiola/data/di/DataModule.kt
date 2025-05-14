package com.mshdabiola.data.di

import com.mshdabiola.analytics.di.analyticsModule
import com.mshdabiola.data.repository.INetworkRepository
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.data.repository.OfflineFirstUserDataRepository
import com.mshdabiola.data.repository.RealINetworkRepository
import com.mshdabiola.data.repository.RealModelRepository
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.database.di.databaseModule
import com.mshdabiola.datastore.di.datastoreModule
import com.mshdabiola.network.di.networkModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule =
    module {
        includes(databaseModule, datastoreModule, networkModule, analyticsModule)
        singleOf(::RealINetworkRepository) bind INetworkRepository::class
        singleOf(::OfflineFirstUserDataRepository) bind UserDataRepository::class
        single { Dispatchers.IO } bind CoroutineDispatcher::class
        singleOf(::RealModelRepository) bind NoteRepository::class
    }
