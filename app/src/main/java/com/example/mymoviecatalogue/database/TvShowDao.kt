package com.example.mymoviecatalogue.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TvShowDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTvShow(tvshowEntity: TvShowEntity): Long

    @Query("SELECT * FROM tTvShow")
    fun getAllTvShow(): Array<TvShowEntity>

    @Query("SELECT * FROM tTvShow WHERE tvshowId = :tvshowId")
    fun getTvShowById(tvshowId: String): Array<TvShowEntity>

    @Query("DELETE FROM tTvShow WHERE tvshowId = :tvshowId")
    fun deleteTvShow(tvshowId: String)
}