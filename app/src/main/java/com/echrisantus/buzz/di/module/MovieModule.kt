package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.moviedetail.MovieDetailFragment
import com.echrisantus.buzz.ui.moviedetail.MovieDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MovieModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun movieDetailFragment(): MovieDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindViewModel(viewmodel: MovieDetailViewModel): ViewModel
}
