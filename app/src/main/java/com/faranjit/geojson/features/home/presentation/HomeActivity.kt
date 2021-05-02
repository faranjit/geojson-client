package com.faranjit.geojson.features.home.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faranjit.geojson.*
import com.faranjit.geojson.databinding.ActivityHomeBinding
import com.faranjit.geojson.features.map.presentation.KiwisActivity
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
        viewModel.languageChangedLiveData.observe(this) {
            viewModel.updateTexts(it)
        }

        prepareUI()
    }

    private fun prepareUI() {
        binding.run {
            imgLanguage.setOnClickListener {
                changeLanguage()
            }

            btnFindKiwi.setOnClickListener {
                startActivity(Intent(this@HomeActivity, KiwisActivity::class.java))
            }
        }
    }

    private fun changeLanguage() {
        viewModel.changeLanguage()
    }
}
