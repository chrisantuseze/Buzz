package com.echrisantus.buzz.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.echrisantus.buzz.database.dao.GenreDao
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.dao.MovieDao
import com.echrisantus.buzz.database.dao.TVDao
import com.echrisantus.buzz.database.model.Genre
import com.echrisantus.buzz.database.model.TV

@Database(entities = [Movie::class, TV::class, Genre::class], version = 1, exportSchema = false)
abstract class BuzzDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TVDao
    abstract fun genreDao(): GenreDao
}