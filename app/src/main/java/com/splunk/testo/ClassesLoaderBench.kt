package com.splunk.testo

import android.os.SystemClock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

class ClassesLoaderBench(
    private val packageName: String,
    private val num: Int
) {

    fun load(): Duration {
        val startNanos = SystemClock.elapsedRealtimeNanos()

        for (index in 0 until num) {
            val suffix = index.toString().padStart(3, '0')
            val className = "$packageName.VersionInfo$suffix"
            val expectedVersion = "1.$suffix.0"
            val clazz = Class.forName(className)
            val version = clazz.getField("VERSION").get(null)?.toString()
                ?: error("Null VERSION field in $className")

            require(version == expectedVersion) {
                "Unexpected VERSION in $className: expected $expectedVersion but found $version"
            }
        }

        return (SystemClock.elapsedRealtimeNanos() - startNanos).nanoseconds
    }
}
