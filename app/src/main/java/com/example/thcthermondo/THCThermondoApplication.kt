package com.example.thcthermondo

import android.app.Application
import com.example.thcthermondo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class THCThermondoApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidLogger()
			androidContext(this@THCThermondoApplication)
			modules(appModule)
		}
	}
}
