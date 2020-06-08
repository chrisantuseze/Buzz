package com.echrisantus.buzz.di.module

import com.echrisantus.buzz.network.service.MovieService
import com.echrisantus.buzz.network.service.TVService
import com.echrisantus.buzz.util.Constants
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideTVApi(retrofit: Retrofit): TVService {
        return retrofit.create(TVService::class.java)
    }
}