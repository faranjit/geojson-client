package com.faranjit.geojson.network

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.util.*

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class TokenConfig(var token: String? = null)

class TokenFeature(val config: TokenConfig) {
    companion object : HttpClientFeature<TokenConfig, TokenFeature> {
        override val key: AttributeKey<TokenFeature> = AttributeKey("TokenFeature")

        override fun install(feature: TokenFeature, scope: HttpClient) {
            feature.config.token?.let { token ->
                scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                    context.url {
                        parameters.append("code", token)
                    }
                }
            }
        }

        override fun prepare(block: TokenConfig.() -> Unit): TokenFeature {
            val config = TokenConfig().apply(block)
            return TokenFeature(config)
        }
    }
}
