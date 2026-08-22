package com.example.thcthermondo.repo

import com.example.thcthermondo.shared.ConflictException
import com.example.thcthermondo.shared.VersionedTemperature

class TemperatureRepo(private val prefsRepo: PrefsRepo) {

	fun loadTemperature() =
		prefsRepo.loadTemperature()

	fun setTemperature(newTemp: Float) {
		// This is the case for manual updates from User
		val latestVersion = loadTemperature().version
		setTemperature(newTemp, latestVersion + 1)
	}

	fun setTemperature(newTemp: Float, newVersion: Int) {
		val latestVersion = prefsRepo.loadTemperature().version
		val newTemperature = VersionedTemperature(limitTemperature(newTemp), newVersion)

		if (newVersion > latestVersion) {
			prefsRepo.saveTemperature(newTemperature)
		} else {
			throw ConflictException(newTemperature)
		}
	}

	private fun limitTemperature(newTemp: Float): Float {
		// Pretend newTemp is being enforced to be within sensible/survivable limits :D
		return newTemp
	}
}