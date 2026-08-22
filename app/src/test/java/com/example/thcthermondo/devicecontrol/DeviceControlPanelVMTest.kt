package com.example.thcthermondo.devicecontrol

import com.example.thcthermondo.repo.TemperatureRepo
import com.example.thcthermondo.shared.ConflictException
import com.example.thcthermondo.shared.VersionedTemperature
import io.mockk.coEvery
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
	private val temperatureSF = MutableStateFlow(initialTemp)
	private val temperatureRepo = mockk<TemperatureRepo>(relaxed = true)

	private lateinit var deviceControlPanelVM: DeviceControlPanelVM

	@Before
	fun setUp() {
		// viewModelScope is backed by Dispatchers.Main, which must be set on the JVM.
		Dispatchers.setMain(UnconfinedTestDispatcher())
		every { temperatureRepo.temperatureSF } returns temperatureSF
		deviceControlPanelVM = DeviceControlPanelVM(temperatureRepo)
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
	}

	@Test
	fun `temperatureSF is delegated straight to the repo`() {
		assertSame(temperatureSF, deviceControlPanelVM.temperatureSF)
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
		temperatureSF.value = VersionedTemperature(temperature = initialTemp, version = 5)

		deviceControlPanelVM.increaseTemp()

		coVerify(timeout = TIMEOUT) { temperatureRepo.setTemperature(initialTemp + TEMP_STEP) }
	}

	@Test
	fun `a ConflictException from the repo is swallowed and does not crash the VM`() {
		// TODO: Replace when handleTempConflict() is implemented
		val initialTemp = 20.5F
		coEvery {
			temperatureRepo.setTemperature(any())
		} throws ConflictException(VersionedTemperature(temperature = initialTemp, version = 1))

		// Should not throw off the caller thread; the VM catches it internally.
		deviceControlPanelVM.increaseTemp()

		coVerify(timeout = TIMEOUT) { temperatureRepo.setTemperature(initialTemp) }
	}
}
