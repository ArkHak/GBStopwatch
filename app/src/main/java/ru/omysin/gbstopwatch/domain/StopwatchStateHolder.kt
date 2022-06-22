package ru.omysin.gbstopwatch.domain

private const val DEFAULT_VALUE = 0L

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timestampMillisecondsFormatter: TimestampMillisecondsFormatter,
) {
    var currentStateFirst: StopwatchState = StopwatchState.Paused(DEFAULT_VALUE)
        private set

//    var currentStateTwo: StopwatchState = StopwatchState.Paused(DEFAULT_VALUE)
//        private set

    fun start() {
        currentStateFirst = stopwatchStateCalculator.calculateRunningState(currentStateFirst)
    }

    fun pause() {
        currentStateFirst = stopwatchStateCalculator.calculatePausedState(currentStateFirst)
    }

    fun stop() {
        currentStateFirst = StopwatchState.Paused(DEFAULT_VALUE)
    }

    fun getStringTimeRepresentationFirst(): String {
        val elapsedTime = when (val currentState = currentStateFirst) {
            is StopwatchState.Paused -> currentState.elapsedTime
            is StopwatchState.Running ->
                elapsedTimeCalculator.calculate(currentState)
        }
        return timestampMillisecondsFormatter.format(elapsedTime)
    }
}