package com.mshdabiola.network

import com.mshdabiola.network.model.SubCategoryListResult

interface ICategoryDataSource {
    suspend fun getSubCategories(
        parentCategoryTitle: String,
        limit: Int,
        continuation: String?
    ): SubCategoryListResult
}