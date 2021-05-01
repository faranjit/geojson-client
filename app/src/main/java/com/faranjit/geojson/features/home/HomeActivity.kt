package com.faranjit.geojson.features.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faranjit.geojson.*
import com.faranjit.geojson.databinding.ActivityHomeBinding
import com.faranjit.geojson.features.map.MapsActivity
import com.faranjit.geojson.language.LanguagePack
import com.faranjit.geojson.language.LanguageResourceProvider
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(ServiceLocator.provideHomeRepository(this))
    }

    private val binding: ActivityHomeBinding by viewBinding(ActivityHomeBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewmodel = viewModel

        prepareUI()
        viewModel.languageChangedLiveData.observe(this, this::updateLanguagePack)
    }

    private fun prepareUI() {
        binding.run {
            imgLanguage.setOnClickListener {
                changeLanguage()
            }

            btnFindKiwi.setOnClickListener {
                startActivity(Intent(this@HomeActivity, MapsActivity::class.java))
            }
        }
    }

    private fun changeLanguage() = if (viewModel.language == KEY_TURKISH) {
        viewModel.language = KEY_ENGLISH
    } else {
        viewModel.language = KEY_TURKISH
    }

    private fun updateLanguagePack(lang: String) {
        readNewLanguagePack(lang)
        viewModel.updateTexts()
    }

    private fun readNewLanguagePack(lang: String) {
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
