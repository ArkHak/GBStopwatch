package ru.omysin.gbstopwatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.omysin.gbstopwatch.model.StopwatchListOrchestrator

private const val DEFAULT_TIME = "00:00:000"

class StopWatchViewModel(
    private val stopwatchListOrchestrator: StopwatchListOrchestrator,
) : ViewModel() {

    private val _stopwatchFirst = MutableStateFlow(DEFAULT_TIME)
    val stopwatchFirst = _stopwatchFirst

    init {
        viewModelScope.launch {
            stopwatchListOrchestrator.tickerFirstStopwatch.collect {
                _stopwatchFirst.tryEmit(it)
            }
        }
    }

    fun start() {
        stopwatchListOrchestrator.start()
    }

    fun pause() {
        stopwatchListOrchestrator.pause()
    }

    fun stop() {
        stopwatchListOrchestrator.stop()
    }
}