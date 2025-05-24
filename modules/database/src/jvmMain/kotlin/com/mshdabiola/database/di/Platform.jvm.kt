package com.mshdabiola.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.mshabiola.database.util.Constant.DATABASE_NAME
import com.mshdabiola.database.WikipediaCommonsDatabase
import com.mshdabiola.model.generalPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val databaseModule: Module
    get() =
        module {
            single {
                getRoomDatabase(getDatabaseBuilder())
            }
            includes(daoModules)
        }

fun getDatabaseBuilder(): RoomDatabase.Builder<WikipediaCommonsDatabase> {
    val path = File(generalPath, DATABASE_NAME)
//    if (path.exists().not()) {
//        path.mkdirs()
//    }
    return Room.databaseBuilder<WikipediaCommonsDatabase>(
        name = path.absolutePath,
    )
    //   .setDriver(BundledSQLiteDriver())
}
