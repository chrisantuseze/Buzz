package com.echrisantus.buzz.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.echrisantus.buzz.database.model.Genre

@Dao
interface GenreDao {
    @Query("SELECT * FROM genre WHERE id = :id")
    suspend fun getGenreById(id: Int): Genre

    @Query("SELECT * FROM genre")
    suspend fun getAllGenres(): List<Genre>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: Genre)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(genres: List<Genre>)

    @Query("DELETE FROM genre")
    suspend fun deleteAll()
}