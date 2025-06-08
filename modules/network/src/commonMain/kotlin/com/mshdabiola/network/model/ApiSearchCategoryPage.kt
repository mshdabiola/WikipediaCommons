package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSearchCategoryPage(
    val pageid: Long,
    val ns: Int,
    val title: String,
    val description: String? = null,
    val descriptionsource: String? = null,
    val thumbnail: ApiThumbnail? = null,
    val pageimage: String? = null, // The actual file name, not a URL
    val categoryinfo: ApiCategoryInfo? = null,
)
