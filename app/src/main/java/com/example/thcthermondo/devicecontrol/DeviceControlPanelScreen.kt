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
	val temperature = deviceControlPanelVM.temperatureSF.collectValue()

	THCThermondoTheme {
		Column(modifier) {
			TemperatureItem(temperature)
			AdjustTempComponent(deviceControlPanelVM)
		}
	}
}