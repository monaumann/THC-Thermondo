package com.example.thcthermondo.devicecontrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thcthermondo.repo.TemperatureRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

private const val TEMP_STEP = 0.5F

class DeviceControlPanelVM(private val temperatureRepo: TemperatureRepo) : ViewModel() {

	val temperatureStateSF get() = temperatureRepo.temperatureStateSF

	private val latestTemp get() = temperatureRepo.temperatureStateSF.value.latestTemp.temperature

	fun increaseTemp() {
		val newTemp = (latestTemp + TEMP_STEP)
		setTemperature(newTemp)
	}

	fun decreaseTemp() {
		val newTemp = (latestTemp - TEMP_STEP)
		setTemperature(newTemp)
	}

	fun setTemperature(newTemp: Float) {
		// To improve Testability Dispatchers.IO should be declared through an injected delegate,
		// so it could be replaced with a TestDispatcher instead of 2sec Timeout in the Test
		viewModelScope.launch(IO) {
			temperatureRepo.setTemperature(newTemp)
		}
	}
}