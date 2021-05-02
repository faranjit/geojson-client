package com.faranjit.geojson.network

import io.ktor.client.*
import io.ktor.client.request.*

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class GeojsonClient(val client: HttpClient) {

    companion object {
        const val BASE_URL = "https://ride-iot-api.azurewebsites.net/api/"
    }

    suspend inline fun <reified T> get(endPoint: String): T =
        client.get {
            url("$BASE_URL$endPoint")
        }

}