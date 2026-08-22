package com.example.thcthermondo.devicecontrol

import androidx.lifecycle.ViewModel
import com.example.thcthermondo.repo.TemperatureRepo
import com.example.thcthermondo.shared.ConflictException
import com.example.thcthermondo.shared.VersionedTemperature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceControlPanelVM(private val temperatureRepo: TemperatureRepo) : ViewModel() {

	private val _temperatureSF = MutableStateFlow(initTemperature())
	val temperatureSF: StateFlow<VersionedTemperature> = _temperatureSF

	fun initTemperature(): VersionedTemperature =
		temperatureRepo.loadTemperature()

	fun setTemperature(newTemp: Float) {
		// TODO: Wrap logic in a Coroutine and make Repo methods suspend functions
		try {
			temperatureRepo.setTemperature(newTemp)
		} catch (throwable: Throwable) {
			if (throwable is ConflictException) {
				// TODO: Handle ConflictException thrown by TemperatureRepo
			}
		}
	}
}