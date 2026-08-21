package com.example.thcthermondo.devicecontrol

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thcthermondo.ui.Greeting
import com.example.thcthermondo.ui.theme.THCThermondoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceControlPanelScreen(modifier: Modifier = Modifier) {
	val deviceControlPanelVM = koinViewModel<DeviceControlPanelVM>()

	THCThermondoTheme {
		Greeting("World", modifier)
	}
}