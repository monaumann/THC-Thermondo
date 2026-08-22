package com.example.thcthermondo.devicecontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thcthermondo.ui.AdjustTempComponent
import com.example.thcthermondo.ui.TemperatureItem
import com.example.thcthermondo.ui.theme.THCThermondoTheme
import com.example.thcthermondo.ui.util.collectValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceControlPanelScreen(modifier: Modifier = Modifier) {
	val deviceControlPanelVM = koinViewModel<DeviceControlPanelVM>()
	val temperatureState = deviceControlPanelVM.temperatureStateSF.collectValue()
	val latestTemp = temperatureState.latestTemp

	THCThermondoTheme {
		Column(modifier) {
			TemperatureItem(latestTemp)
			AdjustTempComponent(deviceControlPanelVM)
		}
	}
	// TODO: On temperatureState == TemperatureConflict -> show Dialog
}