package com.echrisantus.buzz.di.component.subcomponents

import com.echrisantus.buzz.di.component.subcomponents.HomeComponent
import com.echrisantus.buzz.di.component.subcomponents.MovieComponent
import com.echrisantus.buzz.di.component.subcomponents.TVComponent
import dagger.Module

// This module tells a Component which are its subcomponents
@Module(
    subcomponents = [
        HomeComponent::class,
        MovieComponent::class,
        TVComponent::class
    ]
)
class AppSubcomponents