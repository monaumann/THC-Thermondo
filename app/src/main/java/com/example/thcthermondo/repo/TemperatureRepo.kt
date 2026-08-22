package com.example.thcthermondo.repo

import com.example.thcthermondo.shared.ConflictException
import com.example.thcthermondo.shared.VersionedTemperature
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TemperatureRepo(private val prefsRepo: PrefsRepo) {

	private val _temperatureSF = MutableStateFlow(loadTemperature())
	val temperatureSF: StateFlow<VersionedTemperature> = _temperatureSF

	fun loadTemperature() =
		prefsRepo.loadTemperature()

	suspend fun setTemperature(newTemp: Float) {
		// This is the case for manual updates from User
		val latestVersion = loadTemperature().version
		setTemperature(newTemp, latestVersion + 1)
	}

	suspend fun setTemperature(newTemp: Float, newVersion: Int) {
		val latestVersion = prefsRepo.loadTemperature().version
		val newTemperature = VersionedTemperature(limitTemperature(newTemp), newVersion)

		if (newVersion > latestVersion) {
			prefsRepo.saveTemperature(newTemperature)
			_temperatureSF.value = newTemperature
		} else {
			throw ConflictException(newTemperature)
		}
	}

	private fun limitTemperature(newTemp: Float): Float {
		// Pretend newTemp is being enforced to be within sensible/survivable limits :D
		return newTemp
	}
}