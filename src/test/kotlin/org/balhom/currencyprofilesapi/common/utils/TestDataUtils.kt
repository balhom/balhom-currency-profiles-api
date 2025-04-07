package org.balhom.currencyprofilesapi.common.utils

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.*
import kotlin.random.Random


class TestDataUtils {

    companion object {
        fun randomBool(): Boolean {
            return Random.nextBoolean()
        }

        fun randomInt(): Int {
            return Random.nextInt()
        }

        fun randomInt(min: Int, max: Int): Int {
            return Random.nextInt((max - min) + 1) + min
        }

        fun randomDouble(): Double {
            return Random.nextDouble()
        }

        fun randomDouble(min: Double, max: Double): Double {
            return Random.nextDouble(max - min) + min
        }

        fun randomBigDecimal(): BigDecimal {
            return BigDecimal(randomDouble())
                .setScale(2, RoundingMode.HALF_UP)
        }

        fun randomBigDecimal(min: Double, max: Double): BigDecimal {
            return BigDecimal(randomDouble(min, max))
                .setScale(2, RoundingMode.HALF_UP)
        }

        fun randomText(): String {
            return randomText(randomInt(0, 1000))
        }

        fun randomText(min: Int, max: Int): String {
            return randomText(randomInt(min, max))
        }

        fun randomText(max: Int): String {
            val useLetters = true
            val useNumbers = false
            return RandomStringUtils
                .random(max, useLetters, useNumbers)
        }

        fun randomEmail(): String {
            return String.format(
                "%s@%s.%s",
                randomText(15),
                randomText(6),
                randomText(3)
            )
        }

        fun randomDateTimeBetween(
            startInclusive: Instant, endExclusive: Instant
        ): LocalDateTime {
            val startSeconds = startInclusive.epochSecond
            val endSeconds = endExclusive.epochSecond
            val randomSeconds: Long = ThreadLocalRandom
                .current()
                .nextLong(startSeconds, endSeconds)
            return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(randomSeconds),
                ZoneOffset.UTC
            )
        }

        fun randomPastDateTime(): LocalDateTime {
            val now = Instant.now()
            val past = Instant.now().minusSeconds(
                randomInt(10, 1000).toLong() * 60 * 24
            )
            return randomDateTimeBetween(past, now)
        }

    }
}