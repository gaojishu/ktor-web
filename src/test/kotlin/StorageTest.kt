package com.example

import io.ktor.server.testing.testApplication
import kotlin.test.*

class StorageTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
    }

}
