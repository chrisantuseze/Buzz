package com.echrisantus.buzz.di.component

import android.content.Context
import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    HomeModule::class,
    MovieModule::class,
    TVModule::class,
    FavouriteModule::class,
    SearchModule::class,
    SplashModule::class
])
interface AppComponent: AndroidInjector<BuzzApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}