package com.echrisantus.buzz.database.dao

import androidx.room.*
import com.echrisantus.buzz.database.model.Movie

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieById(id: Int): Movie

    @Query("SELECT * FROM movie WHERE category = :category LIMIT :page")
    suspend fun getMoviesByCategory(category: String, page: Int): List<Movie>

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(movies: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteAll()

    @Query("DELETE FROM movie WHERE favourite = :del")
    suspend fun deleteAllFav(del: Boolean)

    @Query("DELETE FROM movie WHERE id = :id")
    suspend fun deleteFavById(id: Int)

    @Transaction
    suspend fun deleteAllAndInsert(movieList: List<Movie>) {
//        Timber.i("DELETING & INSERTING DATA")
        deleteAll()
        insertList(movieList)
    }
}