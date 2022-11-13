package com.lizhaotailang.packman.common.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

object KtorClient {

    val httpClient = HttpClient {
        install(plugin = ContentNegotiation) {
            json(json = json)
        }
        install(plugin = Logging) {
            level = LogLevel.BODY
        }
    }

}
