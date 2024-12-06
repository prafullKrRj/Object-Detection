package com.prafullkumar.objectdetector.di


import com.prafullkumar.objectdetector.data.manager.datastore.LocalUserConfigManagerImpl
import com.prafullkumar.objectdetector.data.manager.objectDetection.ObjectDetectionManagerImpl
import com.prafullkumar.objectdetector.domain.manager.datastore.LocalUserConfigManager
import com.prafullkumar.objectdetector.domain.manager.objectDetection.ObjectDetectionManager
import com.prafullkumar.objectdetector.domain.usecases.detection.DetectObjectUseCase
import com.prafullkumar.objectdetector.domain.usecases.userconfig.ReadUserConfig
import com.prafullkumar.objectdetector.domain.usecases.userconfig.UserConfigUseCases
import com.prafullkumar.objectdetector.domain.usecases.userconfig.WriteUserConfig
import com.prafullkumar.objectdetector.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {


    single<LocalUserConfigManager> {
        LocalUserConfigManagerImpl(get())
    }
    single<ObjectDetectionManager> {
        ObjectDetectionManagerImpl(get())
    }
    single<UserConfigUseCases> {
        UserConfigUseCases(
            readUserConfig = ReadUserConfig(get()),
            writeUserConfig = WriteUserConfig(get())
        )
    }
    single<DetectObjectUseCase> {
        DetectObjectUseCase(
            objectDetectionManager = get()
        )
    }
    viewModel { HomeViewModel() }
}