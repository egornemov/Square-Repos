package com.nemov.squarerepos.util

import com.nemov.squarerepos.AppExecutors

import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}
