package com.echrisantus.buzz.di.module

import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.di.factory.ViewModelBuilder
import com.echrisantus.buzz.di.factory.ViewModelKey
import com.echrisantus.buzz.ui.favourite.FavouriteDetailFragment
import com.echrisantus.buzz.ui.favourite.FavouriteFragment
import com.echrisantus.buzz.ui.favourite.FavouriteViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FavouriteModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun favouriteFragment(): FavouriteFragment

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun favouriteDetailFragment(): FavouriteDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel::class)
    abstract fun bindViewModel(viewmodel: FavouriteViewModel): ViewModel
}