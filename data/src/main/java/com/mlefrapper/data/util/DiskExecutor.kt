package com.mlefrapper.data.util

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskExecutor : Executor {

    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }
}
