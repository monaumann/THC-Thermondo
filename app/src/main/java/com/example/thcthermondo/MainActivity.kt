package com.example.thcthermondo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.thcthermondo.devicecontrol.DeviceControlPanelScreen
import com.example.thcthermondo.navigation.AppDestination
import com.example.thcthermondo.ui.NavIcon
import com.example.thcthermondo.ui.theme.THCThermondoTheme
import com.example.thcthermondo.ui.theme.paddingM

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			THCThermondoTheme {
				THCThermondoApp()
			}
		}
	}
}

@PreviewScreenSizes
@Composable
fun THCThermondoApp() {
	// Normally I'd extract the logic to handle destination state to dedicated VM (overkill atm :D)
	var currentDestination by rememberSaveable { mutableStateOf(AppDestination.HOME) }

	NavigationSuiteScaffold(
		navigationSuiteItems = {
			AppDestination.entries.forEach {
				item(
					icon = { NavIcon(it) },
					label = { Text(it.label) },
					selected = it == currentDestination,
					onClick = { currentDestination = it }
				)
			}
		}
	) {
		Scaffold(modifier = Modifier.fillMaxSize().padding(paddingM)) { innerPadding ->
			DeviceControlPanelScreen(
				modifier = Modifier.padding(innerPadding)
			)
		}
	}
}
