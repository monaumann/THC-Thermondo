package com.example.thcthermondo.devicecontrol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thcthermondo.repo.TemperatureRepo
import com.example.thcthermondo.shared.ConflictException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

private const val TEMP_STEP = 0.5F

class DeviceControlPanelVM(private val temperatureRepo: TemperatureRepo) : ViewModel() {

	val temperatureSF get() = temperatureRepo.temperatureSF

	fun increaseTemp() {
		val newTemp = (temperatureSF.value.temperature + TEMP_STEP)
		setTemperature(newTemp)
	}

	fun decreaseTemp() {
		val newTemp = (temperatureSF.value.temperature - TEMP_STEP)
		setTemperature(newTemp)
	}

	private fun setTemperature(newTemp: Float) {
		// To improve Testability Dispatchers.IO should be declared through an injected delegate,
		// so it could be replaced with a TestDispatcher instead of 2sec Timeout in the Test
		viewModelScope.launch(IO) {
			try {
				temperatureRepo.setTemperature(newTemp)
			} catch (throwable: Throwable) {
				if (throwable is ConflictException) {
					handleTempConflict(throwable)
				}
			}
		}
	}

	private fun handleTempConflict(conflict: ConflictException) {
		// TODO: Handle the Temperature Conflict according to Collaborative On/Off
	}
}