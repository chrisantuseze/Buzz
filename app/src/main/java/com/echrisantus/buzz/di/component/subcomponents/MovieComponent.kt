package com.echrisantus.buzz.di.component.subcomponents

import com.echrisantus.buzz.di.ActivityScope
import com.echrisantus.buzz.ui.home.HomeFragment
import com.echrisantus.buzz.ui.moviedetail.MovieDetailFragment
import dagger.Subcomponent

@ActivityScope
// Definition of a Dagger subcomponent
@Subcomponent
interface MovieComponent {
    // Factory to create instances of LoginComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): MovieComponent
    }

    // Classes that can be injected by this Component
    fun inject(fragment: MovieDetailFragment)
}