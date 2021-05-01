package com.faranjit.geojson.language

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Bulent Turkmen on 1.05.2021.
 */
class LanguageResourceProviderTest {

    @Test
    fun shouldGetStringReturnsSuccessEnglish() {
        // Given
        val languagePackEn: LanguagePack = Json.decodeFromString(ENGLISH_PACK)
        LanguageResourceProvider.setDictionary(languagePackEn.dictionary)

        // When
        val title = LanguageResourceProvider.getString("home.title")

        // Then
        assertEquals("Where is the Kiwi?", title)
    }

    @Test
    fun shouldGetStringReturnsSuccessTurkish() {
        // Given
        val languagePackEn: LanguagePack = Json.decodeFromString(TURKISH_PACK)
        LanguageResourceProvider.setDictionary(languagePackEn.dictionary)

        // When
        val title = LanguageResourceProvider.getString("home.title")

        // Then
        assertEquals("Kiwi Nerede?", title)
    }

    @Test
    fun shouldChangeLanguagePack() {
        // Given
        val languagePackEn: LanguagePack = Json.decodeFromString(ENGLISH_PACK)
        LanguageResourceProvider.setDictionary(languagePackEn.dictionary)

        // When
        val titleEn = LanguageResourceProvider.getString("home.title")

        val languagePackTr: LanguagePack = Json.decodeFromString(TURKISH_PACK)
        LanguageResourceProvider.setDictionary(languagePackTr.dictionary)

        val titleTr = LanguageResourceProvider.getString("home.title")

        // Then
        assertEquals("Where is the Kiwi?", titleEn)
        assertEquals("Kiwi Nerede?", titleTr)
    }
}

private val ENGLISH_PACK =
    """
        {
  "app_name": "GeojsonClient",
  "entities": {
    "home": {
      "title": "Where is the Kiwi?",
      "yes": "Yes"
    },
    "map": {
      "title": "Map"
    }
  }
}
    """.trimIndent()

private val TURKISH_PACK =
    """
        {
  "app_name": "GeojsonClient",
  "entities": {
    "home": {
      "title": "Kiwi Nerede?",
      "yes": "Evet"
    },
    "map": {
      "title": "Harita"
    }
  }
}
    """.trimIndent()