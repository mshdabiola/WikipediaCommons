/*
 *abiola 2024
 */

package com.mshdabiola.data.model

import com.mshdabiola.database.model.MainImageEntity
import com.mshdabiola.database.model.NoteEntity
import com.mshdabiola.model.MainImage
import com.mshdabiola.model.Note

fun Note.asNoteEntity() = NoteEntity(if (id == -1L) null else id, title, content)

fun NoteEntity.asNote() = Note(id ?: -1, title, content)

fun MainImage.asMainImageEntity(page: Int) =
    MainImageEntity(
        title = title,
        mime = mime,
        sha1 = sha1,
        url = url,
        user = user,
        page = page,
    )

fun MainImageEntity.asMainImage() =
    MainImage(
        title = title,
        mime = mime,
        sha1 = sha1,
        url = url,
        user = user,
    )
