package com.mshdabiola.network

import UploadResponseWrapper

interface IUploadDataSource {
    /**
     * Uploads a file (or a chunk of it) to a temporary stash.
     * Corresponds to @/.wikipediaclient/Upload/FileToStash.md
     *
     * @param token CSRF token for the upload.
     * @param filename The desired filename on the wiki.
     * @param fileData The byte array of the file or chunk.
     * @param fileSize The total size of the file being uploaded (not just the chunk).
     * @param offset The byte offset if uploading in chunks. Should be 0 for the first chunk.
     * @param fileKey The key for the stashed upload, returned by previous chunk uploads. Null for the first chunk.
     * @return [UploadResponseWrapper] containing the API response.
     */
    suspend fun uploadFileToStash(
        token: String,
        filename: String,
        fileData: ByteArray,
        fileSize: Long,
        offset: Long?,
        fileKey: String?,
    ): UploadResponseWrapper

    /**
     * Completes an upload from a previously stashed file (or file chunks).
     * Corresponds to @/.wikipediaclient/Upload/UploadFromStash.md
     *
     * @param token CSRF token for the upload.
     * @param fileKey The key of the stashed file to upload from.
     * @param filename The final filename for the upload.
     * @param comment Optional edit summary for the upload.
     * @param text Optional wikitext for the file's description page.
     * @return [UploadResponseWrapper] containing the API response.
     */
    suspend fun uploadFromStash(
        token: String,
        fileKey: String,
        filename: String,
        comment: String?,
        text: String?,
    ): UploadResponseWrapper
}
