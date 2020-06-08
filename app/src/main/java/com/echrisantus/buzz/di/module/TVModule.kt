package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.tv.TVSeriesDetailViewModel
import com.echrisantus.buzz.ui.tv.TVSeriesDetailFragment
import com.echrisantus.buzz.ui.tv.TvSeriesMoreDetailsFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TVModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun tvFragment(): TVSeriesDetailFragment

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun tvMoreDetailsFragment(): TvSeriesMoreDetailsFragment

    @Binds
    @IntoMap
    @ViewModelKey(TVSeriesDetailViewModel::class)
    abstract fun bindViewModel(viewmodel: TVSeriesDetailViewModel): ViewModel
}