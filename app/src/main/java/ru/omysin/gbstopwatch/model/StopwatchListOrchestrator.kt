package ru.omysin.gbstopwatch.model

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.omysin.gbstopwatch.domain.StopwatchStateHolder

private const val DEFAULT_TIME = "00:00:000"
private const val TICKER_DELAY = 20L

class StopwatchListOrchestrator(
    private val stopwatchStateHolder: StopwatchStateHolder,
    private val scope: CoroutineScope
) {
    private var job: Job? = null
    private val mutableFirstTicker = MutableStateFlow(DEFAULT_TIME)
    val tickerFirstStopwatch: StateFlow<String> = mutableFirstTicker

    fun start() {
        if (job == null) startJob()
        stopwatchStateHolder.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                mutableFirstTicker.value = stopwatchStateHolder.getStringTimeRepresentationFirst()
                delay(TICKER_DELAY)
            }
        }
    }

    fun pause() {
        stopwatchStateHolder.pause()
        stopJob()
    }

    fun stop() {
        stopwatchStateHolder.stop()
        stopJob()
        clearValue()
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
        mutableFirstTicker.value = DEFAULT_TIME
    }
}