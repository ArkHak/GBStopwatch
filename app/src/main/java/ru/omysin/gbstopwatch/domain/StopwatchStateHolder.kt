package ru.omysin.gbstopwatch.domain

import ru.omysin.gbstopwatch.domain.StopwatchNumber.*

private const val DEFAULT_VALUE = 0L

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter,
) {
    var currentStateFirst: StopwatchState = StopwatchState.Paused(DEFAULT_VALUE)
        private set

    var currentStateSecond: StopwatchState = StopwatchState.Paused(DEFAULT_VALUE)
        private set

    fun start(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                currentStateFirst =
                    stopwatchStateCalculator.calculateRunningState(currentStateFirst)
            }
            STOPWATCH_SECOND -> {
                currentStateSecond =
                    stopwatchStateCalculator.calculateRunningState(currentStateSecond)
            }
            STOPWATCH_ALL -> {
                start(STOPWATCH_FIRST)
                start(STOPWATCH_SECOND)
            }
        }
    }

    fun pause(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                currentStateFirst = stopwatchStateCalculator.calculatePausedState(currentStateFirst)
            }
            STOPWATCH_SECOND -> {
                currentStateSecond =
                    stopwatchStateCalculator.calculatePausedState(currentStateSecond)

            }
            STOPWATCH_ALL -> {
                pause(STOPWATCH_FIRST)
                pause(STOPWATCH_SECOND)
            }
        }
    }

    fun stop(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                currentStateFirst = StopwatchState.Paused(DEFAULT_VALUE)
            }
            STOPWATCH_SECOND -> {
                currentStateSecond = StopwatchState.Paused(DEFAULT_VALUE)
            }
            STOPWATCH_ALL -> {
                stop(STOPWATCH_FIRST)
                stop(STOPWATCH_SECOND)
            }
        }
    }

    fun getStringTimeRepresentationFirst(): String {
        val elapsedTime = when (val currentState = currentStateFirst) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running ->
                elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }

    fun getStringTimeRepresentationSecond(): String {
        val elapsedTime = when (val currentState = currentStateSecond) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running ->
                elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}