package com.mshdabiola.network

import com.mshdabiola.network.model.EditResponseWrapper
import com.mshdabiola.network.model.SetLabelResponseWrapper // New import

interface IEditDataSource {
    /**
     * Submits an edit to a page.
     * Corresponds to @PostEdit.md
     */
    suspend fun postEdit(
        title: String,
        summary: String,
        text: String,
        token: String,
    ): EditResponseWrapper

    /**
     * Creates a new page or edits an existing one, with more content options.
     * Corresponds to @PostCreate.md
     */
    suspend fun postCreatePage(
        title: String,
        summary: String,
        text: String,
        contentFormat: String,
        contentModel: String,
        isMinorEdit: Boolean?,
        recreatePage: Boolean?,
        token: String,
    ): EditResponseWrapper

    /**
     * Appends text to a page.
     * Corresponds to @PostAppend.md
     */
    suspend fun postAppendText(
        title: String,
        summary: String,
        appendText: String,
        token: String,
    ): EditResponseWrapper

    /**
     * Prepends text to a page.
     * Corresponds to @PostPrepend.md
     */
    suspend fun postPrependText(
        title: String,
        summary: String,
        prependText: String,
        token: String,
    ): EditResponseWrapper

    /**
     * Adds a new section to a page.
     * Corresponds to @NewSection.md
     */
    suspend fun postNewSection(
        title: String,
        summary: String,
        sectionTitle: String,
        text: String,
        token: String,
    ): EditResponseWrapper

    /**
     * Sets a label (e.g., caption) for a Wikidata entity or a MediaWiki page (like a File).
     * Corresponds to @PostCaption.md
     * The 'title' parameter here usually refers to the entity ID (e.g., "Q42" or "M123") or
     * a page title like "File:Example.jpg".
     * The 'value' is the actual caption/label text.
     */
    suspend fun postCaption(
        title: String,
        language: String,
        value: String,
        summary: String?,
        token: String,
    ): SetLabelResponseWrapper
}
