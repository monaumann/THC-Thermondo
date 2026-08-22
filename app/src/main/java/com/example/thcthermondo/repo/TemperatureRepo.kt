package com.example.thcthermondo.repo

import com.example.thcthermondo.shared.TemperatureState
import com.example.thcthermondo.shared.TemperatureState.TemperatureConflict
import com.example.thcthermondo.shared.TemperatureState.ValidTemperature
import com.example.thcthermondo.shared.VersionedTemperature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TemperatureRepo(private val prefsRepo: PrefsRepo) {

	private val _temperatureStateSF = MutableStateFlow<TemperatureState>(initTempState())
	val temperatureStateSF: StateFlow<TemperatureState> = _temperatureStateSF

	val latestTemp get() = temperatureStateSF.value.latestTemp

	private fun initTempState(): ValidTemperature {
		val latestTemp = prefsRepo.loadTemperature()
		return ValidTemperature(latestTemp)
	}

	suspend fun setTemperature(newTemp: Float) {
		// This is the case for manual updates from User and version is only tracked in the Repo
		val latestVersion = latestTemp.version
		setTemperature(newTemp, latestVersion + 1)
	}

	suspend fun setTemperature(newTemperature: Float, newVersion: Int) {
		val latestVersion = latestTemp.version
		val newTemp = VersionedTemperature(limitTemperature(newTemperature), newVersion)

		if (newVersion > latestVersion) {
			prefsRepo.saveTemperature(newTemp)
			emitValidTemp(newTemp)
		} else {
			emitTempConflict(latestTemp, newTemp)
		}
	}

	private fun limitTemperature(newTemp: Float): Float {
		// Pretend newTemp is being enforced to be within sensible/survivable limits :D
		return newTemp
	}

	private fun emitValidTemp(latestTemp: VersionedTemperature) {
		_temperatureStateSF.value = ValidTemperature(latestTemp = latestTemp)
	}

	private fun emitTempConflict(latestTemp: VersionedTemperature, newTemp: VersionedTemperature) {
		_temperatureStateSF.value = TemperatureConflict(latestTemp = latestTemp, newTemp = newTemp)
	}
}