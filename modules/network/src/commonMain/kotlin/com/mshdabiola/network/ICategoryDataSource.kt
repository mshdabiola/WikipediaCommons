package com.mshdabiola.network

import com.mshdabiola.network.model.SubCategoryListResult
import com.mshdabiola.network.model.SearchedCategoryInfo

interface ICategoryDataSource {
    suspend fun getSubCategories(
        parentCategoryTitle: String,
        limit: Int,
        continuation: String?
    ): SubCategoryListResult

    suspend fun searchCategories(
        searchTerm: String,
        limit: Int,
        offset: Int
    ): List<SearchedCategoryInfo>
}