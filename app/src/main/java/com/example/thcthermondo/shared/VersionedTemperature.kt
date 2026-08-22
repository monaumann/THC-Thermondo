package com.example.thcthermondo.shared

data class VersionedTemperature(val temperature: Float, val version: Int)

sealed class TemperatureState(open val latestTemp: VersionedTemperature) {
	data class ValidTemperature(override val latestTemp: VersionedTemperature): TemperatureState(latestTemp)
	data class TemperatureConflict(
		override val latestTemp: VersionedTemperature,
		val newTemp: VersionedTemperature
	): TemperatureState(newTemp)
}