package com.faranjit.geojson

import android.app.Application
import com.faranjit.geojson.language.LanguagePack
import com.faranjit.geojson.language.LanguageResourceProvider
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        initLanguagePack(ServiceLocator.provideHomeRepository(this).getLanguage())
    }

    // If you want to, you can read language file from your server
    private fun initLanguagePack(lang: String) {
        with(assets.open("lang_$lang.json")) {
            val buffer = ByteArray(available())
            read(buffer)
            val json = String(buffer).trimIndent()
            val languagePack: LanguagePack = Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
            }.decodeFromString(json)
            LanguageResourceProvider.setDictionary(languagePack.dictionary)
            close()
        }
    }
}