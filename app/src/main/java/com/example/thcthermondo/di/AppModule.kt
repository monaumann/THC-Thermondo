package com.example.thcthermondo.di

import com.example.thcthermondo.devicecontrol.DeviceControlPanelVM
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Root Koin module for the app. Register singletons, factories and view models here.
 */
val appModule = module {
	 viewModel { DeviceControlPanelVM() }
}
