package com.mshdabiola.data.repository

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.IMediaDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RealMediaRepository(
    private val mediaDataSource: IMediaDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : IMediaRepository{
    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
       return withContext(ioDispatcher){
            mediaDataSource.getAllImages(limit)


        }
    }
}