package com.example.thcthermondo.di

import com.example.thcthermondo.devicecontrol.DeviceControlPanelVM
import com.example.thcthermondo.repo.TemperatureRepo
import com.example.thcthermondo.repo.PrefsRepo
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Root Koin module for the app. Register singletons, factories and view models here.
 */
val appModule = module {
	single { PrefsRepo(androidApplication()) }
	single { TemperatureRepo(get()) }

	viewModel { DeviceControlPanelVM(get()) }
}
