@file:Suppress("DEPRECATION")

package com.example.kamandanoe.viewmodeltest

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}
