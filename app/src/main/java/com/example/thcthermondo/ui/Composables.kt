package com.example.thcthermondo.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.thcthermondo.navigation.AppDestination
import com.example.thcthermondo.ui.theme.THCThermondoTheme

@Composable
fun NavIcon(destination: AppDestination) {
	Icon(destination.icon, contentDescription = destination.label)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	THCThermondoTheme {
		Greeting("World")
	}
}