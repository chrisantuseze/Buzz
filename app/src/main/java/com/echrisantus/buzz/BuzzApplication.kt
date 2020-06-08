package com.echrisantus.buzz

import android.content.Context
import com.echrisantus.buzz.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class BuzzApplication: DaggerApplication() {
    init {
        instance = this
    }

    companion object {
        private var instance: BuzzApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}