package com.kevinaiken.starter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// https://www.reddit.com/r/Kotlin/comments/8gbiul/slf4j_loggers_in_3_ways/dyalq6s/
inline fun <reified T> T.createLogger(): Logger {
    if (T::class.isCompanion) {
        return LoggerFactory.getLogger(T::class.java.enclosingClass)
    }
    return LoggerFactory.getLogger(T::class.java)
}
