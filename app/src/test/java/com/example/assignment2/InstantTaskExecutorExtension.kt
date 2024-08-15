package com.example.assignment2

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.AfterEachCallback

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    private val instantTaskExecutor = object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) = runnable.run()
        override fun postToMainThread(runnable: Runnable) = runnable.run()
        override fun isMainThread(): Boolean = true
    }

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(instantTaskExecutor)
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}
