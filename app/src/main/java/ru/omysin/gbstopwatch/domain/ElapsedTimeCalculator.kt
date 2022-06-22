package ru.omysin.gbstopwatch.domain

private const val DEFAULT_VALUE = 0L

class ElapsedTimeCalculator(private val timestampProvider: TimestampProvider) {
    fun calculate(state: StopwatchState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            DEFAULT_VALUE
        }
        return timePassedSinceStart + state.elapsedTime
    }
}