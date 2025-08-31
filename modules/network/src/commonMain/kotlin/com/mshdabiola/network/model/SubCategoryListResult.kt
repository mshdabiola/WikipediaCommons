package com.mshdabiola.network.model

data class SubCategoryListResult(
    val subCategories: List<SubCategoryInfo>,
    val continuationToken: String?,
)
