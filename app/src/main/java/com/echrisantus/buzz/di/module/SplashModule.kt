package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.splash.SplashScreen
import com.echrisantus.buzz.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SplashModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun splasScreen(): SplashScreen

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindViewModel(viewmodel: SplashViewModel): ViewModel
}