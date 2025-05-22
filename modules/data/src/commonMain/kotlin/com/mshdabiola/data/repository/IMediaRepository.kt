package com.mshdabiola.data.repository

import com.mshdabiola.model.MainImage

interface IMediaRepository {

   suspend fun getAllMedia(page: Int, limit: Int): List<MainImage>
}