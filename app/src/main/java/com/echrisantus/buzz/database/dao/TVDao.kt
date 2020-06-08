package com.echrisantus.buzz.database.dao

import androidx.room.*
import com.echrisantus.buzz.database.model.TV

@Dao
interface TVDao {
    @Query("SELECT * FROM tv WHERE id = :id")
    suspend fun getTVById(id: Int): TV?

    @Query("SELECT * FROM tv WHERE category = :category LIMIT :page")
    suspend fun getTvByCategory(category: String, page: Int): List<TV>

    @Query("SELECT * FROM tv")
    suspend fun getAllTv(): List<TV>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvSeries: TV)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(tvSeriesList: List<TV>)

    @Query("DELETE FROM tv")
    suspend fun deleteAll()

    @Query("DELETE FROM tv WHERE favourite = :del")
    suspend fun deleteAllFav(del: Boolean)

    @Query("DELETE FROM tv WHERE id = :id")
    suspend fun deleteFavById(id: Int)


    @Transaction
    suspend fun deleteAllAndInsert(tvSeriesList: List<TV>) {
//        Timber.i("DELETING & INSERTING DATA")
        deleteAll()
        insertList(tvSeriesList)
    }
}