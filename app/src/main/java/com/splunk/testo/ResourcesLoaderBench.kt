package com.splunk.testo

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class ResourcesLoaderBench {

    fun load(): Duration {
        Thread.sleep(2500)
        return 2500.milliseconds
    }
}