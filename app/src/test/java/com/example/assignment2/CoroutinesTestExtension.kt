package com.example.assignment2

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import org.junit.jupiter.api.extension.TestInstancePreDestroyCallback

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTestExtension(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestInstancePostProcessor, TestInstancePreDestroyCallback {

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun preDestroyTestInstance(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
