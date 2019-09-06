package com.example.mymoviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import com.example.mymoviecatalogue.database.Contract.AUTHORITY
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.CONTENT_URI
import com.example.mymoviecatalogue.database.Contract.Columns.Companion.ID
import com.example.mymoviecatalogue.database.Contract.TABLE_NAME
import com.example.mymoviecatalogue.database.Helper

class FavoriteProvider : ContentProvider() {

    private var helper: Helper? = null

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val database = helper!!.writableDatabase
        val match = uriMatcher.match(p0)
        val insertUri: Uri

        when (match) {
            MOVIES -> {
                val id = database.insert(TABLE_NAME, null, p1)

                if(id > 0) {
                    insertUri = ContentUris.withAppendedId(CONTENT_URI, id)
                } else {
                    throw SQLException("Failed to insert row into $p0")
                }
            } else -> throw UnsupportedOperationException("Unknown Uri: $p0")
        }

        context!!.contentResolver.notifyChange(p0, null)

        return insertUri
    }

    override fun query(p0: Uri, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor? {
        var selection = p2
        var selectionArgs = p3

        val database = helper!!.readableDatabase
        val match = uriMatcher.match(p0)

        val cursor: Cursor

        when (match) {
            MOVIES, MOVIES_WITH_ID -> {
                if (match == MOVIES_WITH_ID) {
                    val id = ContentUris.parseId(p0)
                    selection = String.format("%s = ?", ID)
                    selectionArgs = arrayOf(id.toString())
                }

                cursor = database.query(TABLE_NAME, p1, selection, selectionArgs, null, null, p4)
            } else -> throw UnsupportedOperationException("Unknown Uri: $p0")
        }
        cursor.setNotificationUri(context!!.contentResolver, p0)
        return cursor
    }

    override fun onCreate(): Boolean {
        helper = context?.let { Helper(it) }
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val database = helper!!.writableDatabase
        val match = uriMatcher.match(p0)

        val update: Int

        when (match) {
            MOVIES -> update = database.update(TABLE_NAME, p1, p2, p3)
            else -> throw UnsupportedOperationException("Unknown Uri : $p0")
        }

        context!!.contentResolver.notifyChange(p0, null)
        return update
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val selection: String?
        var selectionArgs = p2

        when (uriMatcher.match(p0)) {
            MOVIES -> selection = p1 ?: "1"
            MOVIES_WITH_ID -> {
                val id = ContentUris.parseId(p0)
                selection = String.format("%s = ?", ID)
                selectionArgs = arrayOf(id.toString())
            }
            else -> throw IllegalArgumentException("Illegal delete Uri")
        }

        val database = helper!!.writableDatabase
        val delete = database.delete(TABLE_NAME, selection, selectionArgs)

        if (delete > 0) {
            context!!.contentResolver.notifyChange(p0, null)
        }

        return delete
    }

    override fun getType(p0: Uri): String? {
        return null
    }

    companion object {
        private const val MOVIES = 100
        private const val MOVIES_WITH_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIES)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", MOVIES_WITH_ID)
        }
    }
}