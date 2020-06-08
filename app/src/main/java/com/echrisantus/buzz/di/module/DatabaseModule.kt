package com.echrisantus.buzz.di.module

import android.content.Context
import androidx.room.Room
import com.echrisantus.buzz.database.db.BuzzDatabase
import com.echrisantus.buzz.util.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideBuzzDB(context: Context): BuzzDatabase {
        return Room.databaseBuilder(context, BuzzDatabase::class.java, Constants.DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(buzzDatabase: BuzzDatabase) =
        buzzDatabase.movieDao()

    @Provides
    @Singleton
    fun provideTVDao(buzzDatabase: BuzzDatabase) =
        buzzDatabase.tvDao()

    @Provides
    @Singleton
    fun provideGenreDao(buzzDatabase: BuzzDatabase) =
        buzzDatabase.genreDao()

}