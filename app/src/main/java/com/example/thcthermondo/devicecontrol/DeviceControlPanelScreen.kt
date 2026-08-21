package com.example.thcthermondo.devicecontrol

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.thcthermondo.ui.Greeting
import com.example.thcthermondo.ui.theme.THCThermondoTheme

@Composable
fun DeviceControlPanelScreen(modifier: Modifier = Modifier) {
	THCThermondoTheme {
		Greeting("World", modifier)
	}
}