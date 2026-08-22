package com.example.thcthermondo.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thcthermondo.shared.DEFAULT_TEMP
import com.example.thcthermondo.navigation.AppDestination
import com.example.thcthermondo.shared.VersionedTemperature
import com.example.thcthermondo.ui.theme.THCThermondoTheme

@Composable
fun NavIcon(destination: AppDestination) {
	Icon(destination.icon, contentDescription = destination.label)
}

@Composable
fun TemperatureItem(latestTemp: VersionedTemperature, modifier: Modifier = Modifier) {
	val temperature = latestTemp.temperature
	val version = latestTemp.version
	Text(
		text = "Latest temperature: $temperature° (ver: $version)",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun TemperatureItemPreview() {
	THCThermondoTheme {
		TemperatureItem(VersionedTemperature(DEFAULT_TEMP, 0))
	}
}