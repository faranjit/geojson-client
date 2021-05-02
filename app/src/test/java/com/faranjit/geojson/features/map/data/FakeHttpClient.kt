package com.faranjit.geojson.features.map.data

import com.faranjit.geojson.network.GeojsonClient
import io.ktor.client.*
import io.ktor.client.engine.mock.*

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
val fakeClient = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            when (request.url.encodedPath) {
                GeojsonClient.BASE_URL -> {
                    respond(dummyKiwiLocationResponse)
                }
                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
    }
}