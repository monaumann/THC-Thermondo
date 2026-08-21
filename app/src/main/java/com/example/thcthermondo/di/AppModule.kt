package com.example.thcthermondo.di

import org.koin.dsl.module

/**
 * Root Koin module for the app. Register singletons, factories and view models here.
 */
val appModule = module {
	// Define dependencies here, e.g.:
	// single<SomeRepository> { SomeRepositoryImpl() }
	// viewModel { SomeViewModel(get()) }
}
