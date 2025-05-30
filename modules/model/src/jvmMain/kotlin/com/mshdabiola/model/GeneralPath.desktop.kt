package com.mshdabiola.model

actual val generalPath: String
    get() = System.getProperty("java.io.tmpdir") + "/wikipediacommons"
// "${System.getProperty("user.home")}/AppData/Local/wikipediacommons"
