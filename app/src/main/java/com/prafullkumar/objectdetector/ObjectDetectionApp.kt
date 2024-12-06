package com.prafullkumar.objectdetector

import android.app.Application
import com.prafullkumar.objectdetector.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ObjectDetectionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ObjectDetectionApp)
            androidLogger()
            modules(appModule)
        }
    }
}