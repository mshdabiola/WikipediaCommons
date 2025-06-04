package com.mshdabiola.data.repository

import com.mshdabiola.data.model.asMainImage
import com.mshdabiola.data.model.asMainImageEntity
import com.mshdabiola.database.dao.MainImageDao
import com.mshdabiola.datastore.Store
import com.mshdabiola.model.MainImage
import com.mshdabiola.network.IMediaDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RealMediaRepository(
    private val mediaDataSource: IMediaDataSource,
    private val mainImageDao: MainImageDao,
    private val store: Store,
    private val ioDispatcher: CoroutineDispatcher,
) : IMediaRepository {
    override val bookmarkSet: Flow<Set<String>>
        get() =
            store
                .userData
                .map { it.bookmarkSet }
                .flowOn(ioDispatcher)
    override val searchHistory: Flow<List<String>>
        get() =
            store
                .userData
                .map { it.searchHistory.reversed() }

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
            saveImagesToDatabase(networkImages, page)

            // Return network results
            networkImages
        }
    }

    override suspend fun toggleBookmark(id: String) {
        withContext(ioDispatcher) {
            store.updateUserData {
                it.copy(
                    bookmarkSet =
                        if (id in it.bookmarkSet) {
                            it.bookmarkSet - id
                        } else {
                            it.bookmarkSet + id
                        },
                )
            }
        }
    }

    override suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage> {
        return withContext(ioDispatcher) {
            mediaDataSource.search(title, page, limit)
        }
    }

    override suspend fun addSearchHistory(search: String) {
        withContext(ioDispatcher) {
            store.updateUserData {
                it.copy(
                    searchHistory =
                        if (search in it.searchHistory) {
                            it.searchHistory
                        } else {
                            val list = it.searchHistory + search

                            list.takeLast(6)
                        },
                )
            }
        }
    }

    override suspend fun clearSearchHistory() {
        withContext(ioDispatcher) {
            store.updateUserData {
                it.copy(
                    searchHistory = emptyList(),
                )
            }
        }
    }

    private suspend fun saveImagesToDatabase(
        images: List<MainImage>,
        page: Int,
    ) {
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
