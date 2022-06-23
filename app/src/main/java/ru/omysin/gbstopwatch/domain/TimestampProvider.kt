package ru.omysin.gbstopwatch.domain

interface TimestampProvider {
    fun getMilliseconds(): Long
}