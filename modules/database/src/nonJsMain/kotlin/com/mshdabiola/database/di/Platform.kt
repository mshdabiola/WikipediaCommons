package com.mshdabiola.database.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mshdabiola.database.WikipediaCommonsDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val daoModules =
    module {
        single {
            get<WikipediaCommonsDatabase>().getNoteDao()
        }

        single {
            get<WikipediaCommonsDatabase>().getImageDao()
        }
    }

fun getRoomDatabase(builder: RoomDatabase.Builder<WikipediaCommonsDatabase>): WikipediaCommonsDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
