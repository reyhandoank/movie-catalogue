package com.example.mymoviecatalogue.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovie(movieEntity: MovieEntity): Long

    @Query("SELECT * FROM tMovie")
    fun getAllMovie(): Array<MovieEntity>

    @Query("SELECT * FROM tMovie WHERE movieId = :movieId")
    fun getMovieById(movieId: String): Array<MovieEntity>

    @Query("DELETE FROM tMovie WHERE movieId = :movieId")
    fun deleteMovie(movieId: String)
}