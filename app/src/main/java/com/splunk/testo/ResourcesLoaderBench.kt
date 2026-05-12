package com.splunk.testo

import android.os.SystemClock
import java.util.Properties
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

class ResourcesLoaderBench(private val num: Int) {

    fun load(): Duration {
        val startNanos = SystemClock.elapsedRealtimeNanos()

        for (index in 0 until num) {
            val suffix = index.toString().padStart(3, '0')
            val resourceName = "/version_info_$suffix.properties"
            val expectedVersion = "1.$suffix.0"
            val properties = Properties()
            val inputStream = checkNotNull(javaClass.getResourceAsStream(resourceName)) {
                "Missing resource: $resourceName"
            }

            inputStream.use { inputStream ->
                properties.load(inputStream)
            }

            val version = properties.getProperty("version")
            require(!version.isNullOrBlank()) { "Missing version property in $resourceName.properties" }
            require(version == expectedVersion) {
                "Unexpected version in $resourceName: expected $expectedVersion but found $version"
            }
        }

        return (SystemClock.elapsedRealtimeNanos() - startNanos).nanoseconds
    }
}
