package com.example.thcthermondo.devicecontrol

import com.example.thcthermondo.repo.TemperatureRepo
import com.example.thcthermondo.shared.TemperatureState.ValidTemperature
import com.example.thcthermondo.shared.VersionedTemperature
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

private const val DEFAULT_TEMP = 20.0F
private const val INITIAL_VERSION = 0
private const val TIMEOUT = 2_000L
private const val TEMP_STEP = 0.5F

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceControlPanelVMTest {

	private val initialTemp = VersionedTemperature(DEFAULT_TEMP, INITIAL_VERSION)
	private val temperatureStateSF = MutableStateFlow(ValidTemperature(initialTemp))
	private val temperatureRepo = mockk<TemperatureRepo>(relaxed = true)

	private lateinit var deviceControlPanelVM: DeviceControlPanelVM

	@Before
	fun setUp() {
		// viewModelScope is backed by Dispatchers.Main, which must be set on the JVM.
		Dispatchers.setMain(UnconfinedTestDispatcher())
		every { temperatureRepo.temperatureStateSF } returns temperatureStateSF
		deviceControlPanelVM = DeviceControlPanelVM(temperatureRepo)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `temperatureSF is delegated straight to the repo`() {
		assertSame(temperatureStateSF, deviceControlPanelVM.temperatureStateSF)
	}

	@Test
	fun `increaseTemp raises the current temperature by the step`() {
		deviceControlPanelVM.increaseTemp()

		coVerify(timeout = TIMEOUT) { temperatureRepo.setTemperature(DEFAULT_TEMP + TEMP_STEP) }
	}

	@Test
	fun `decreaseTemp lowers the current temperature by the step`() {
		deviceControlPanelVM.decreaseTemp()

		coVerify(timeout = TIMEOUT) { temperatureRepo.setTemperature(DEFAULT_TEMP - TEMP_STEP) }
	}

	@Test
	fun `temperature step is applied relative to the latest emitted value`() {
		val initialTemp = 30.0F
		val latestTemp = VersionedTemperature(temperature = initialTemp, version = 5)
		temperatureStateSF.value = ValidTemperature(latestTemp)

		deviceControlPanelVM.increaseTemp()

		coVerify(timeout = TIMEOUT) { temperatureRepo.setTemperature(initialTemp + TEMP_STEP) }
	}
}
