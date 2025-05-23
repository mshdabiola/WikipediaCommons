/*
 *abiola 2024
 */

package com.mshdabiola.data.model

import com.mshdabiola.database.model.NoteEntity
import com.mshdabiola.model.MainImage
import com.mshdabiola.model.Note
import com.mshdabiola.network.model.Imageinfo

fun Note.asNoteEntity() = NoteEntity(if (id == -1L) null else id, title, content)

fun NoteEntity.asNote() = Note(id ?: -1, title, content)

fun Imageinfo.toMainImage() = MainImage(
    canonicaltitle,
    descriptionshorturl,
    descriptionurl,
    mime,
    sha1,
    url,
    user
)
