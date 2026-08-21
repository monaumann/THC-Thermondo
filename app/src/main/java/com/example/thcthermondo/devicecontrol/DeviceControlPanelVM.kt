package com.example.thcthermondo.devicecontrol

import androidx.lifecycle.ViewModel
import com.example.thcthermondo.constants.DEFAULT_TEMP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceControlPanelVM : ViewModel() {

	private val _temperatureSF = MutableStateFlow(DEFAULT_TEMP)
	val temperatureSF: StateFlow<Float> = _temperatureSF

	fun setTemperature(newTemp: Float) {
		// TODO: Set newTemp in Repo
		// TODO: Handle ConflictException thrown by FakeRepo
	}
}