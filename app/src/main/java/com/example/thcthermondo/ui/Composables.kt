package com.example.thcthermondo.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.tooling.preview.Preview
import com.example.thcthermondo.devicecontrol.DeviceControlPanelVM
import com.example.thcthermondo.shared.DEFAULT_TEMP
import com.example.thcthermondo.navigation.AppDestination
import com.example.thcthermondo.shared.TemperatureState.TemperatureConflict
import com.example.thcthermondo.shared.VersionedTemperature
import com.example.thcthermondo.ui.theme.THCThermondoTheme
import com.example.thcthermondo.ui.theme.paddingS

@Composable
fun NavIcon(destination: AppDestination) {
	Icon(destination.icon, contentDescription = destination.label)
}

@Composable
fun TemperatureItem(latestTemp: VersionedTemperature) {
	val temperature = latestTemp.temperature
	val version = latestTemp.version
	Text(
		text = "Temperature: $temperature° (ver: $version)"
	)
}

@Composable
fun AdjustTempComponent(deviceControlPanelVM: DeviceControlPanelVM) {
	Row(verticalAlignment = CenterVertically) {
		Button(
			modifier = Modifier.padding(end = paddingS),
			onClick = { deviceControlPanelVM.increaseTemp() }
		) {
			Text("+")
		}
		Button(
			modifier = Modifier.padding(end = paddingS),
			onClick = { deviceControlPanelVM.decreaseTemp() }
		) {
			Text("-")
		}
	}
}

@Composable
fun ResolveConflictDialog(
	deviceControlPanelVM: DeviceControlPanelVM,
	conflict: TemperatureConflict
) {
	val newTemp = conflict.newTemp
	val latestTemp = conflict.latestTemp
	val technicianTemp = newTemp.temperature
	val userTemp = latestTemp.temperature

	AlertDialog(
		onDismissRequest = {},
		text = { Text(text = formatText(technicianTemp, userTemp)) },
		confirmButton = { ResolveButton(deviceControlPanelVM, newTemp, "Keep theirs") },
		dismissButton = { ResolveButton(deviceControlPanelVM, latestTemp, "Overwrite") }
	)
}

@Composable
private fun ResolveButton(
	deviceControlPanelVM: DeviceControlPanelVM,
	versionedTemp: VersionedTemperature,
	text: String
) {
	Button(onClick = { updateTemperature(deviceControlPanelVM, versionedTemp) }) {
		Text(text)
	}
}

private fun formatText(technicianTemp: Float, userTemp: Float) =
	"Remote change detected! The technician just set this to $technicianTemp°C. \n" +
	"Do you want to keep their change or overwrite it with $userTemp°C?"

private fun updateTemperature(
	deviceControlPanelVM: DeviceControlPanelVM,
	versionedTemp: VersionedTemperature
) {
	val newTemp = versionedTemp.temperature
	val newVersion = versionedTemp.version + 1
	deviceControlPanelVM.setTemperature(newTemp, newVersion)
}

@Preview(showBackground = true)
@Composable
fun TemperatureItemPreview() {
	THCThermondoTheme {
		TemperatureItem(VersionedTemperature(DEFAULT_TEMP, 0))
	}
}