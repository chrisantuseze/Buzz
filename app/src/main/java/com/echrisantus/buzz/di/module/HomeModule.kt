package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.home.HomeFragment
import com.echrisantus.buzz.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun homeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(viewmodel: HomeViewModel): ViewModel
}