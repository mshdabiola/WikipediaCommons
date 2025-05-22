package com.mshdabiola.data.repository

import com.mshdabiola.data.model.toMainImage
import com.mshdabiola.model.MainImage
import com.mshdabiola.network.IMediaDataSource
import com.mshdabiola.network.INetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RealMediaRepository(
    private val mediaDataSource: IMediaDataSource,
    private val networkData : INetworkDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : IMediaRepository{
    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
       return withContext(ioDispatcher){
            mediaDataSource.getAllImages(limit)
                .mapNotNull { it?.imageinfo}
                .flatten()
                .mapNotNull { it?.toMainImage() }

        }
    }
}