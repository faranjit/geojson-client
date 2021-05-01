package com.faranjit.geojson.features.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH
import com.faranjit.geojson.R
import com.faranjit.geojson.features.home.domain.HomeRepository
import com.faranjit.geojson.getOrAwaitValue
import com.faranjit.geojson.language.LanguagePack
import com.faranjit.geojson.language.LanguageResourceProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel

    private lateinit var homeRepository: HomeRepository

    @Before
    fun setup() {
        val languagePack = LanguagePack(
            appName = "Geojson",
            dictionary = mapOf(
                "home" to linkedMapOf(
                    "title" to "Where is Kiwi?",
                    "yes" to "Yes"
                )
            )
        )
        LanguageResourceProvider.setDictionary(languagePack.dictionary)

        homeRepository = FakeHomeRepository(KEY_ENGLISH)
        viewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun verifyViewModelInited() {
        assertEquals(KEY_ENGLISH, viewModel.languageChangedLiveData.getOrAwaitValue())
    }

    @Test
    fun verifyLanguageChaned() {
        // Given
        val languagePack = LanguagePack(
            appName = "Geojson",
            dictionary = mapOf(
                "home" to linkedMapOf(
                    "title" to "Kiwi nerede?",
                    "yes" to "Bul"
                )
            )
        )

        // When
        viewModel.language = KEY_ENGLISH
        viewModel.changeLanguage()
        viewModel.updateTexts(KEY_TURKISH)

        // Then
        assertEquals(KEY_TURKISH, viewModel.language)
        assertEquals(KEY_TURKISH, viewModel.languageChangedLiveData.getOrAwaitValue())
        assertEquals(R.drawable.turkish, viewModel.languageFlagObservable.get())
    }

    @Test
    fun verifyUpdateTexts() {
        // Given
        val languagePack = LanguagePack(
            appName = "Geojson",
            dictionary = mapOf(
                "home" to linkedMapOf(
                    "title" to "Kiwi nerede?",
                    "yes" to "Bul"
                )
            )
        )

        // When
        LanguageResourceProvider.setDictionary(languagePack.dictionary)
        viewModel.updateTexts("tr")

        // Then
        assertEquals("Kiwi nerede?", viewModel.titleObservable.get())
        assertEquals("Bul", viewModel.buttonObservable.get())
    }
}