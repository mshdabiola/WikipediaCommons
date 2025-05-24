package com.mshdabiola.network.model

import com.mshdabiola.model.MainImage


internal fun Imageinfo.toMainImage() = MainImage(
    cleanImageTitle(canonicaltitle),
    mime,
    sha1,
    url,
    user
)


private fun cleanImageTitle(title: String): String {
    var cleanedTitle = title
    if (cleanedTitle.startsWith("File:", ignoreCase = true)) {
        cleanedTitle = cleanedTitle.removePrefix("File:")
    }
    // You might want to handle various image extensions
    val extensionsToRemove = listOf(".jpg", ".jpeg", ".png", ".gif", ".webp") // Add more as needed
    for (ext in extensionsToRemove) {
        if (cleanedTitle.endsWith(ext, ignoreCase = true)) {
            cleanedTitle = cleanedTitle.removeSuffix(ext)
            break // Assuming only one extension needs to be removed
        }
    }
    return cleanedTitle.trim() // Trim any leading/trailing whitespace that might result
}
