package ru.itis.hw6.di

import dagger.Component
import ru.itis.hw6.presentation.screens.infoCardScreen.InfoCardScreenViewModel
import ru.itis.hw6.presentation.screens.mainScreen.MainScreenViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun mainScreenViewModel(): MainScreenViewModel
    fun infoCardViewModelFactory(): InfoCardScreenViewModel.Factory
}
