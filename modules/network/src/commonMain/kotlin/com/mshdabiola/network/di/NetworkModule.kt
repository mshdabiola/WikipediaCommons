package com.mshdabiola.network.di

import com.mshdabiola.network.IMediaDataSource
import com.mshdabiola.network.INetworkDataSource
import com.mshdabiola.network.MediaDataSource
import com.mshdabiola.network.NetworkDataSource
import io.ktor.util.network.NetworkAddress
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule =
    module {

        singleOf(::httpClient)
        singleOf(::MediaDataSource) bind IMediaDataSource::class
        singleOf(::NetworkDataSource) bind INetworkDataSource::class
    }
