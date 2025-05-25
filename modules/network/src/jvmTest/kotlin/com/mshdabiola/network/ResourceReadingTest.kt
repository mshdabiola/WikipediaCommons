package com.mshdabiola.network

import java.io.File
import java.nio.charset.Charset
import kotlin.io.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.text.trim

fun readFileFromTestResources(fileName: String): String? {
    return try {
        // Get the classloader for the current class (or any class in your test source set)
        val classLoader = java.lang.Thread.currentThread().contextClassLoader

        // Get the URL of the resource file
        val resourceUrl = classLoader.getResource(fileName)

        if (resourceUrl != null) {
            // Convert the URL to a File (or use resourceUrl.openStream() for a stream)
            val file = File(resourceUrl.toURI())

            // Read the file content as a String
            file.readText(Charset.defaultCharset()) // Specify charset if needed
        } else {
            kotlin.io.println("Resource file '$fileName' not found in test resources.")
            null
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        null
    }
}

class ResourceReadingTest {
    @Test
    fun testReadingExampleFile() {
        val fileContent = readFileFromTestResources("test.json")

        assertNotNull(fileContent, "Should be able to read the file.")
        assertEquals("This is an example test resource file.", fileContent?.trim())
    }

    @Test
    fun testReadingNonExistentFile() {
        val fileContent = readFileFromTestResources("non_existent_file.txt")
        assertEquals(null, fileContent, "Should return null for a non-existent file.")
    }
}
