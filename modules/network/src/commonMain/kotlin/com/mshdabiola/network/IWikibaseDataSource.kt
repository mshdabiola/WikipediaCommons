package com.mshdabiola.network

import com.mshdabiola.network.model.WikibaseEditEntityResponse
import com.mshdabiola.network.model.WikibaseGetClaimsResponse
import com.mshdabiola.network.model.WikibaseQueryFileEntityResponse
import com.mshdabiola.network.model.WikibaseSetLabelResponse

interface IWikibaseDataSource {

    /**
     * Edits a Wikibase entity using its ID.
     * Corresponds to @/.wikipediaclient/Wikibase/EditEntity.md
     */
    suspend fun editEntityById(
        id: String,
        token: String,
        data: String, // JSON string of claims, labels, etc.
    ): WikibaseEditEntityResponse

    /**
     * Edits a Wikibase entity (typically MediaInfo) using a file title on Commons.
     * Corresponds to @/.wikipediaclient/Wikibase/EditEntityByFilename.md
     */
    suspend fun editEntityByFilename(
        title: String, // e.g., "File:MyImage.jpg"
        token: String,
        data: String, // JSON string of claims, labels, etc.
    ): WikibaseEditEntityResponse

    /**
     * Fetches basic info for a file entity using its title.
     * Corresponds to @/.wikipediaclient/Wikibase/FileEntityById.md (prop=info)
     * Note: The .md file uses 'titles' which implies it's using the standard wiki API, not necessarily a Wikibase specific one like wbgetentities.
     * The URL {{BaseUrl}} also points to standard API.
     */
    suspend fun getFileEntityInfoByTitle(
        title: String, // e.g., "File:MyImage.jpg"
    ): WikibaseQueryFileEntityResponse

    /**
     * Adds or updates a label for a Wikidata entity.
     * Corresponds to @/.wikipediaclient/Wikibase/AddLabelToWikiData.md (wbsetlabel)
     */
    suspend fun setWikidataLabel(
        id: String, // Wikidata entity ID (e.g., "Q42")
        token: String,
        language: String,
        value: String,
    ): WikibaseSetLabelResponse

    /**
     * Fetches claims for a specific property of an entity.
     * Corresponds to @/.wikipediaclient/Wikibase/ClaimByProperty.md (wbgetclaims)
     */
    suspend fun getClaimsByProperty(
        entityId: String,
        propertyId: String,
    ): WikibaseGetClaimsResponse

    /**
     * Deletes claims from an entity.
     * Corresponds to @/.wikipediaclient/Wikibase/PostDeleteClaims.md
     * This is specified as a GET request with 'data' and 'token' in the URL,
     * which is unusual for an edit operation but will be implemented as specified.
     */
    suspend fun deleteClaims(
        id: String, // Entity ID
        token: String,
        data: String, // JSON string specifying claims to remove
    ): WikibaseEditEntityResponse
}