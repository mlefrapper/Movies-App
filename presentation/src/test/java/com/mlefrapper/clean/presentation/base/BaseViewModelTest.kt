package com.mlefrapper.clean.presentation.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mlefrapper.clean.presentation.util.rules.CoroutineTestRule
import com.mlefrapper.clean.presentation.util.rules.runTest
import kotlinx.coroutines.test.TestScope
import org.junit.Rule

open class BaseViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    fun runTest(block: suspend TestScope.() -> Unit) = coroutineRule.runTest(block)
}
