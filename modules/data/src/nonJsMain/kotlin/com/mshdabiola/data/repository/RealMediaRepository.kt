package com.mshdabiola.data.repository

import com.mshdabiola.data.model.asMainImage
import com.mshdabiola.data.model.asMainImageEntity
import com.mshdabiola.database.dao.MainImageDao
import com.mshdabiola.model.MainImage
import com.mshdabiola.network.IMediaDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RealMediaRepository(
    private val mediaDataSource: IMediaDataSource,
    private val mainImageDao: MainImageDao,
    private val ioDispatcher: CoroutineDispatcher,
) : IMediaRepository {
    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
        return withContext(ioDispatcher) {

            val imagesFromDatabase = getImagesFromDatabase(page)

            if (imagesFromDatabase.isNotEmpty()) {
                // Return from database
                return@withContext imagesFromDatabase
            }



            // Fetch from network
            val networkImages = mediaDataSource.getAllImages(limit)

            // Save to database
            saveImagesToDatabase(networkImages,page)

            // Return network results
            networkImages
        }
    }

    private suspend fun saveImagesToDatabase(images: List<MainImage>,page:Int) {
        withContext(ioDispatcher) {
            val imageEntities = images.map { it.asMainImageEntity(page) }
            mainImageDao.insertAll(imageEntities)
        }
    }

    suspend fun getImagesFromDatabase(page: Int): List<MainImage> {
        return withContext(ioDispatcher) {
            mainImageDao.getByPage(page).map { it.asMainImage() }
        }
    }
}
