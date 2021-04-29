package com.faranjit.geojson.features.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faranjit.geojson.R
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Bulent Turkmen on 29.04.2021.
 */
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    companion object {
        private const val PACKAGE_NAME = "com.faranjit.geojson"
        private const val MAPS_ACTIVITY_NAME = ".MapsActivity"
    }

    @get:Rule
    var activityRule: ActivityScenarioRule<HomeActivity> =
        ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun verifyMapsActivityOpened() {
        // When
        onView(withId(R.id.btn_find_kiwi)).perform(click())

        // Then
        intended(
            allOf(
                hasComponent(hasShortClassName(MAPS_ACTIVITY_NAME)),
                toPackage(PACKAGE_NAME)
            )
        )
    }
}