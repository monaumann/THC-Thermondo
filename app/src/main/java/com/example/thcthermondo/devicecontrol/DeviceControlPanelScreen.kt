package com.example.thcthermondo.devicecontrol

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thcthermondo.devicecontrol.CollaborativeMode.OFF
import com.example.thcthermondo.devicecontrol.CollaborativeMode.ON
import com.example.thcthermondo.shared.TemperatureState.TemperatureConflict
import com.example.thcthermondo.ui.AdjustTempComponent
import com.example.thcthermondo.ui.ResolveConflictDialog
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
	if (temperatureState is TemperatureConflict) {
		// TODO: Implement Toggle for Collaborative Mode ON/OFF
		val collaborativeMode = OFF

		if (collaborativeMode == ON) {
			deviceControlPanelVM.setTemperature(temperatureState.newTemp.temperature)
			// TODO: Show Toast/Snackbar
		} else {
			ResolveConflictDialog(deviceControlPanelVM, temperatureState)
		}
	}
}

private sealed class CollaborativeMode {
	object ON: CollaborativeMode()
	object OFF: CollaborativeMode()
}