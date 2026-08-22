package com.example.thcthermondo.devicecontrol

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thcthermondo.repo.TemperatureRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

private const val TEMP_STEP = 0.5F

class DeviceControlPanelVM(private val temperatureRepo: TemperatureRepo) : ViewModel() {

	// This version state only exists to artificially create Conflicts with technician updates
	private val _versionSF = mutableIntStateOf(temperatureRepo.latestTemp.version)
	private val nextVersion get() = _versionSF.intValue + 1

	val temperatureStateSF get() = temperatureRepo.temperatureStateSF

	private val latestTemp get() = temperatureRepo.temperatureStateSF.value.latestTemp.temperature

	fun increaseTemp() {
		val newTemp = (latestTemp + TEMP_STEP)
		setTemperature(newTemp, nextVersion)
	}

	fun decreaseTemp() {
		val newTemp = (latestTemp - TEMP_STEP)
		setTemperature(newTemp, nextVersion)
	}

	fun setTemperature(newTemp: Float, version: Int) {
		_versionSF.intValue = version
		// To improve Testability Dispatchers.IO should be declared through an injected delegate,
		// so it could be replaced with a TestDispatcher instead of 2sec Timeout in the Test
		viewModelScope.launch(IO) {
			temperatureRepo.setTemperature(newTemp, version)
		}
	}
}