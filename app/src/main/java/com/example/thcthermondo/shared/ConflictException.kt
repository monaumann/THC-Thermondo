package com.example.thcthermondo.shared

private const val TEMP_CONFLICT_ERROR = "A conflict occurred between user input and remote settings"

class ConflictException(
	val temperature: VersionedTemperature,
	val errorMsg: String = TEMP_CONFLICT_ERROR
): Throwable()