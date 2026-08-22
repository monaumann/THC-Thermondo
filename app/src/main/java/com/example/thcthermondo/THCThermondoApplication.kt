package com.example.thcthermondo

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.example.thcthermondo.di.appModule
import com.example.thcthermondo.repo.TemperatureRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import kotlin.time.Duration.Companion.milliseconds

private const val REFRESH_INTERVAL_MS = 15_000L

class THCThermondoApplication : Application(), KoinComponent {

	private lateinit var temperatureRepo: TemperatureRepo
	private lateinit var _latestVersion: MutableState<Int>

	// App-scoped: survives configuration changes and lives as long as the process.
	private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
	private var temperatureRefreshJob: Job? = null

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidLogger()
			androidContext(this@THCThermondoApplication)
			modules(appModule)
		}
		temperatureRepo = get()
		_latestVersion = mutableIntStateOf(temperatureRepo.latestTemp.version)
		startTemperatureRefresh()
	}

	private fun startTemperatureRefresh() {
		temperatureRefreshJob = applicationScope.launch {
			while (isActive) {
				delay(REFRESH_INTERVAL_MS.milliseconds)
				updateTemp()
			}
		}
	}

	private suspend fun updateTemp() {
		val latestTemp = temperatureRepo.latestTemp.temperature
		val newTemp = newTemperature(latestTemp)
		_latestVersion.value += 1
		temperatureRepo.setTemperature(newTemp, _latestVersion.value)
	}

	private fun newTemperature(currentTemp: Float): Float {
		val isEven = (0..10).random() % 2 == 0
		val offset = randomOffset()
		val newTemp = if (isEven) currentTemp + offset else currentTemp - offset
		return newTemp
	}

	private fun randomOffset(): Float =
		when ((1..5).random()) {
			5 -> 0.5F
			4 -> 0.4F
			3 -> 0.3F
			2 -> 0.2F
			else -> 0.1F
		}

	override fun onTerminate() {
		super.onTerminate()
		applicationScope.cancel()
		temperatureRefreshJob?.cancel()
	}
}
