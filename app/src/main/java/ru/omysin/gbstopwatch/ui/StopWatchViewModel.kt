package ru.omysin.gbstopwatch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.omysin.gbstopwatch.domain.StopwatchNumber
import ru.omysin.gbstopwatch.model.StopwatchListOrchestrator

private const val DEFAULT_TIME = "00:00:000"

class StopWatchViewModel(
    private val stopwatchListOrchestrator: StopwatchListOrchestrator,
) : ViewModel() {

    private val _stopwatchFirst = MutableStateFlow(DEFAULT_TIME)
    val stopwatchFirst = _stopwatchFirst

    private val _stopwatchSecond = MutableStateFlow(DEFAULT_TIME)
    val stopwatchSecond = _stopwatchSecond

    init {
        viewModelScope.launch {
            stopwatchListOrchestrator.tickerFirstStopwatch.collect {
                _stopwatchFirst.tryEmit(it)
            }
        }
        viewModelScope.launch {
            stopwatchListOrchestrator.tickerSecondStopwatch.collect {
                stopwatchSecond.tryEmit(it)
            }
        }
    }

    fun start(stopwatchNumber: StopwatchNumber) {
        stopwatchListOrchestrator.start(stopwatchNumber)
    }

    fun pause(stopwatchNumber: StopwatchNumber) {
        stopwatchListOrchestrator.pause(stopwatchNumber)
    }

    fun stop(stopwatchNumber: StopwatchNumber) {
        stopwatchListOrchestrator.stop(stopwatchNumber)
    }
}