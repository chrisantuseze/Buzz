package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.home.HomeFragment
import com.echrisantus.buzz.ui.home.HomeViewModel
import com.echrisantus.buzz.ui.search.SearchFragment
import com.echrisantus.buzz.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
abstract class SearchModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun searchFragment(): SearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchViewModel): ViewModel
}