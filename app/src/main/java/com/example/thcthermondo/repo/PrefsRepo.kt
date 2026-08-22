package com.example.thcthermondo.repo

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.thcthermondo.shared.DEFAULT_TEMP
import com.example.thcthermondo.shared.VersionedTemperature

private const val PREFS_NAME = "THC-Thermondo"

private const val INITIAL_VERSION = 0

private const val KEY_LATEST_TEMP = "LATEST_TEMP"
private const val KEY_LATEST_VERSION = "LATEST_VERSION"

class PrefsRepo(application: Application) {

	private val sharedPrefs = application.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

	fun loadTemperature(): VersionedTemperature {
		// Ideally the Data should be serialized as LatestTemperature and NOT in two separate fields
		val latestTemp = sharedPrefs.getFloat(KEY_LATEST_TEMP, DEFAULT_TEMP)
		val latestVersion = sharedPrefs.getInt(KEY_LATEST_VERSION, INITIAL_VERSION)
		return VersionedTemperature(latestTemp, latestVersion)
	}

	fun saveTemperature(newTemperature: VersionedTemperature) {
		// Ideally the Data should be serialized as LatestTemperature and NOT in two separate fields
		saveTemperature(newTemperature.temperature)
		saveVersion(newTemperature.version)
	}

	private fun saveTemperature(newTemp: Float) {
		sharedPrefs.edit { putFloat(KEY_LATEST_TEMP, newTemp) }
	}

	private fun saveVersion(newVersion: Int) {
		sharedPrefs.edit { putInt(KEY_LATEST_VERSION, newVersion) }
	}
}