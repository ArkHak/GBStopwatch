package ru.omysin.gbstopwatch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.omysin.gbstopwatch.databinding.StopwatchActivityBinding

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

        CoroutineScope(Dispatchers.Main + SupervisorJob())
            .launch {
                viewModel.stopwatchFirst.collect {
                    binding.textTime.text = it
                }
            }

        binding.buttonStart.setOnClickListener {
            viewModel.start()
        }

        binding.buttonPause.setOnClickListener {
            viewModel.pause()
        }

        binding.buttonStop.setOnClickListener {
            viewModel.stop()
        }
    }
}