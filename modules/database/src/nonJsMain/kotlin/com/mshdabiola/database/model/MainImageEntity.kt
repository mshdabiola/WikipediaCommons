package com.mshdabiola.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_image_table")
data class MainImageEntity(
    val title: String,
    val mime: String,
    @PrimaryKey
    val sha1: String,
    val url: String,
    val user: String,
    val page:Int
)
