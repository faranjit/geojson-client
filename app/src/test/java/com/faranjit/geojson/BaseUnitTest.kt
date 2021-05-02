package com.faranjit.geojson

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Created by Bulent Turkmen on 2.05.2021.
 */
@ExperimentalCoroutinesApi
abstract class BaseUnitTest {

    val dispatcher = TestCoroutineDispatcher()
    val testScope = TestCoroutineScope(dispatcher)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(dispatcher)
}