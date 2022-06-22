package ru.omysin.gbstopwatch.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.omysin.gbstopwatch.domain.*
import ru.omysin.gbstopwatch.model.StopwatchListOrchestrator
import ru.omysin.gbstopwatch.ui.StopWatchViewModel

val appModule = module {
    viewModel { StopWatchViewModel(get(qualifier = named("stopwatch_orchestrator"))) }

    single(qualifier = named("stopwatch_orchestrator")) {
        StopwatchListOrchestrator(
            get(qualifier = named("stopwatch_state_holder")),
            get(qualifier = named("coroutine_scope"))
        )
    }

    single(qualifier = named("stopwatch_state_holder")) {
        StopwatchStateHolder(
            StopwatchStateCalculator(
                TimestampProviderImpl,
                get(qualifier = named("elapsed_time_calculator"))
            ),
            get(qualifier = named("elapsed_time_calculator")),
            get(qualifier = named("timestamp_milliseconds_formatter"))
        )
    }

    single(qualifier = named("timestamp_provider")) {
        object : TimestampProvider {
            override fun getMilliseconds(): Long {
                return System.currentTimeMillis()
            }
        }
    }

    single(qualifier = named("elapsed_time_calculator")) {
        ElapsedTimeCalculator(TimestampProviderImpl)
    }

    single(qualifier = named("timestamp_milliseconds_formatter")) {
        TimestampMillisecondsFormatter()
    }

    factory(qualifier = named("coroutine_scope")) {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}

object TimestampProviderImpl : TimestampProvider {
    override fun getMilliseconds(): Long {
        return System.currentTimeMillis()
    }
}
