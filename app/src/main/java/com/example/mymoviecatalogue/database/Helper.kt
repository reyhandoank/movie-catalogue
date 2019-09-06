package com.example.mymoviecatalogue.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Helper internal constructor(context: Context) :
        SQLiteOpenHelper(context, "db_favorite", null, 18) {
    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database?.execSQL("DROP TABLE IF EXISTS " + Contract.TABLE_NAME)
        onCreate(database)
    }

    companion object {
        private val SQL_CREATE_TABLE = String.format(
                "CREATE TABLE %s" +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "UNIQUE (%s) ON CONFLICT REPLACE)",

                Contract.TABLE_NAME,
                Contract.Columns.ID,
                Contract.Columns.MOVIE_ID,
                Contract.Columns.MOVIE_TITLE,
                Contract.Columns.MOVIE_DATE,
                Contract.Columns.MOVIE_RATE,
                Contract.Columns.MOVIE_POSTER,
                Contract.Columns.MOVIE_ID
        )
    }
}