package com.example.thcthermondo.repo

import com.example.thcthermondo.shared.TemperatureState.TemperatureConflict
import com.example.thcthermondo.shared.TemperatureState.ValidTemperature
import com.example.thcthermondo.shared.VersionedTemperature
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val DEFAULT_TEMP = 20.0F
private const val INITIAL_VERSION = 0

@OptIn(ExperimentalCoroutinesApi::class)
class TemperatureRepoTest {

	private val initialTemp = VersionedTemperature(temperature = DEFAULT_TEMP, version = INITIAL_VERSION)
	private val prefsRepo = mockk<PrefsRepo>(relaxed = true)

	private lateinit var repo: TemperatureRepo

	@Before
	fun setUp() {
		every { prefsRepo.loadTemperature() } returns initialTemp
		repo = TemperatureRepo(prefsRepo)
	}

	@Test
	fun `initial state is a ValidTemperature loaded from prefs`() {
		assertEquals(ValidTemperature(initialTemp), repo.temperatureStateSF.value)
		assertEquals(initialTemp, repo.latestTemp)
	}

	@Test
	fun `manual setTemperature bumps the version and persists the new value`() = runTest {
		val newTemp = 21.0F
		val newVersion = 1
		repo.setTemperature(newTemp)

		val expected = VersionedTemperature(newTemp, newVersion)
		assertEquals(ValidTemperature(expected), repo.temperatureStateSF.value)
		assertEquals(expected, repo.latestTemp)
		coVerify(exactly = 1) { prefsRepo.saveTemperature(expected) }
	}

	@Test
	fun `a newer version is accepted, persisted and emitted as valid`() = runTest {
		val newTemp = 22.0F
		val newVersion = 3
		repo.setTemperature(newTemp, newVersion)

		val expected = VersionedTemperature(newTemp, newVersion)
		assertEquals(ValidTemperature(expected), repo.temperatureStateSF.value)
		coVerify(exactly = 1) { prefsRepo.saveTemperature(expected) }
	}

	@Test
	fun `an equal version is rejected as a conflict and not persisted`() = runTest {
		val newTemp = 25.0F
		val newVersion = 0
		repo.setTemperature(newTemp, newVersion)

		val rejectedTemp = VersionedTemperature(newTemp, newVersion)
		assertEquals(
			TemperatureConflict(latestTemp = initialTemp, newTemp = rejectedTemp),
			repo.temperatureStateSF.value
		)
		assertEquals(initialTemp, repo.latestTemp)
		coVerify(exactly = 0) { prefsRepo.saveTemperature(any()) }
	}

	@Test
	fun `an older version is rejected as a conflict and not persisted`() = runTest {
		val acceptedTemp = 23.0F
		val acceptedVersion = 5
		repo.setTemperature(acceptedTemp, acceptedVersion)

		val rejectedTemp = 24.0F
		val rejectedVersion = 2
		repo.setTemperature(rejectedTemp, rejectedVersion)

		val accepted = VersionedTemperature(acceptedTemp, acceptedVersion)
		val rejected = VersionedTemperature(rejectedTemp, rejectedVersion)
		assertEquals(
			TemperatureConflict(latestTemp = accepted, newTemp = rejected),
			repo.temperatureStateSF.value
		)

		coVerify(exactly = 1) { prefsRepo.saveTemperature(any()) }
	}
}
