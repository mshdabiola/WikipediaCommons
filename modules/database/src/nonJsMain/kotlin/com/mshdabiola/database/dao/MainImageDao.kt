/*
 *abiola 2024
 */

package com.mshdabiola.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mshdabiola.database.model.MainImageEntity

@Dao
interface MainImageDao {
    @Upsert
    suspend fun insertAll(mainImages: List<MainImageEntity>)

    @Query("SELECT * FROM main_image_table")
    suspend fun getAll(): List<MainImageEntity>

    @Query("DELETE FROM main_image_table")
    suspend fun clearAll()

    @Query("SELECT * FROM main_image_table WHERE page = :page")
    suspend fun getByPage(page: Int): List<MainImageEntity>
}
