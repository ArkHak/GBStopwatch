package ru.omysin.gbstopwatch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.omysin.gbstopwatch.databinding.StopwatchActivityBinding
import ru.omysin.gbstopwatch.domain.StopwatchNumber.*

class StopWatchActivity : AppCompatActivity() {

    private lateinit var binding: StopwatchActivityBinding

    private val viewModel: StopWatchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StopwatchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        initCoroutineScope()

        initView()
    }

    private fun initView() {
        initViewFirstStopwatch()
        initViewSecondStopwatch()
        initViewAllStopwatch()
    }

    private fun initViewFirstStopwatch() {
        binding.buttonStartFirst.setOnClickListener {
            viewModel.start(STOPWATCH_FIRST)
        }

        binding.buttonPauseFirst.setOnClickListener {
            viewModel.pause(STOPWATCH_FIRST)
        }

        binding.buttonStopFirst.setOnClickListener {
            viewModel.stop(STOPWATCH_FIRST)
        }
    }

    private fun initViewSecondStopwatch() {
        binding.buttonStartSecond.setOnClickListener {
            viewModel.start(STOPWATCH_SECOND)
        }

        binding.buttonPauseSecond.setOnClickListener {
            viewModel.pause(STOPWATCH_SECOND)
        }

        binding.buttonStopSecond.setOnClickListener {
            viewModel.stop(STOPWATCH_SECOND)
        }
    }

    private fun initViewAllStopwatch() {
        binding.buttonStartAll.setOnClickListener {
            viewModel.start(STOPWATCH_ALL)
        }

        binding.buttonPauseAll.setOnClickListener {
            viewModel.pause(STOPWATCH_ALL)
        }

        binding.buttonStopAll.setOnClickListener {
            viewModel.stop(STOPWATCH_ALL)
        }
    }

    private fun initCoroutineScope() {
        CoroutineScope(Dispatchers.Main + SupervisorJob())
            .launch {
                viewModel.stopwatchFirst.collect {
                    binding.textTimeFirst.text = it
                }
            }

        CoroutineScope(Dispatchers.Main + SupervisorJob())
            .launch {
                viewModel.stopwatchSecond.collect {
                    binding.textTimeSecond.text = it
                }
            }
    }
}