package com.mshdabiola.network

import com.mshdabiola.network.model.SearchedCategoryInfo
import com.mshdabiola.network.model.SubCategoryInfo
import com.mshdabiola.network.model.SubCategoryListResult

interface ICategoryDataSource {
    suspend fun getSubCategories(
        parentCategoryTitle: String,
        limit: Int,
        continuation: String?,
    ): SubCategoryListResult

    suspend fun searchCategories(
        searchTerm: String,
        limit: Int,
        offset: Int,
    ): List<SearchedCategoryInfo>

    suspend fun searchCategoriesByPrefix(
        prefix: String,
        limit: Int,
        offset: Int,
    ): List<SearchedCategoryInfo>

    suspend fun searchCategoriesByName(
        from: String,
        to: String?,
        limit: Int,
        offset: Int,
    ): List<SearchedCategoryInfo>

    suspend fun searchCategoriesByTitle(
        title: String,
        limit: Int,
    ): List<SearchedCategoryInfo>

    suspend fun getCategoriesInfoByTitles(
        titles: String,
        limit: Int,
    ): List<SubCategoryInfo>
}
