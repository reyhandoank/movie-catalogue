package com.example.mymoviecatalogue.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, TvShowEntity::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDao
    abstract fun tvshowDao() : TvShowDao

    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "movie.db"
                )
                        .fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return INSTANCE as AppDatabase
        }
    }
}