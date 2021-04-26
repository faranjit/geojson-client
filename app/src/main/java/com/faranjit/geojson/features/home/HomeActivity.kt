package com.faranjit.geojson.features.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.faranjit.geojson.R
import com.faranjit.geojson.databinding.ActivityHomeBinding

/**
 * Created by Bulent Turkmen on 26.04.2021.
 */
class HomeActivity: AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        binding.viewmodel = viewModel
    }
}