package ru.omysin.gbstopwatch.model

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.omysin.gbstopwatch.domain.StopwatchNumber
import ru.omysin.gbstopwatch.domain.StopwatchNumber.*
import ru.omysin.gbstopwatch.domain.StopwatchStateHolder

private const val DEFAULT_TIME = "00:00:000"
private const val TICKER_DELAY = 20L

class StopwatchListOrchestrator(
    private val stopwatchStateHolder: StopwatchStateHolder,
    private val scopeFirst: CoroutineScope,
    private val scopeSecond: CoroutineScope
) {
    private var jobFirst: Job? = null
    private var jobSecond: Job? = null
    private val mutableFirstTicker = MutableStateFlow(DEFAULT_TIME)
    private val mutableSecondTicker = MutableStateFlow(DEFAULT_TIME)
    val tickerFirstStopwatch: StateFlow<String> = mutableFirstTicker
    val tickerSecondStopwatch: StateFlow<String> = mutableSecondTicker

    fun start(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                if (jobFirst == null) startJob(stopwatchNumber)
                stopwatchStateHolder.start(stopwatchNumber)
            }
            STOPWATCH_SECOND -> {
                if (jobSecond == null) startJob(stopwatchNumber)
                stopwatchStateHolder.start(stopwatchNumber)
            }
            STOPWATCH_ALL -> {
                start(STOPWATCH_FIRST)
                start(STOPWATCH_SECOND)
            }
        }
    }

    private fun startJob(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                scopeFirst.launch {
                    while (isActive) {
                        mutableFirstTicker.value =
                            stopwatchStateHolder.getStringTimeRepresentationFirst()
                        delay(TICKER_DELAY)
                    }
                }
            }
            STOPWATCH_SECOND -> {
                scopeSecond.launch {
                    while (isActive) {
                        mutableSecondTicker.value =
                            stopwatchStateHolder.getStringTimeRepresentationSecond()
                        delay(TICKER_DELAY)
                    }
                }
            }
        }
    }

    fun pause(stopwatchNumber: StopwatchNumber) {
        stopwatchStateHolder.pause(stopwatchNumber)
        stopJob(stopwatchNumber)
    }

    fun stop(stopwatchNumber: StopwatchNumber) {
        stopwatchStateHolder.stop(stopwatchNumber)
        stopJob(stopwatchNumber)
        clearValue(stopwatchNumber)
    }

    private fun stopJob(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> {
                scopeFirst.coroutineContext.cancelChildren()
//                jobFirst = null
            }
            STOPWATCH_SECOND -> {
                scopeSecond.coroutineContext.cancelChildren()
//                jobSecond = null
            }
            STOPWATCH_ALL -> {
                stopJob(STOPWATCH_FIRST)
                stopJob(STOPWATCH_SECOND)
            }
        }
    }

    private fun clearValue(stopwatchNumber: StopwatchNumber) {
        when (stopwatchNumber) {
            STOPWATCH_FIRST -> mutableFirstTicker.value = DEFAULT_TIME
            STOPWATCH_SECOND -> mutableSecondTicker.value = DEFAULT_TIME
            STOPWATCH_ALL -> {
                clearValue(STOPWATCH_FIRST)
                clearValue(STOPWATCH_SECOND)
            }
        }
    }
}