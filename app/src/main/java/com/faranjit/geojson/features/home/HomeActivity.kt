package com.faranjit.geojson.features.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faranjit.geojson.KEY_ENGLISH
import com.faranjit.geojson.KEY_TURKISH
import com.faranjit.geojson.ServiceLocator
import com.faranjit.geojson.databinding.ActivityHomeBinding
import com.faranjit.geojson.features.map.MapsActivity
import com.faranjit.geojson.viewBinding
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
        viewModel.languageChangedLiveData.observe(this, {
            changeLocale(it)
        })
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

    private fun changeLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        createConfigurationContext(config)
    }
}
