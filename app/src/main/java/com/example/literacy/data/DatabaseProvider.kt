package com.example.literacy.data

import android.content.Context
import androidx.room.Room
import com.example.literacy.data.schema.AppDatabase

object DatabaseProvider {

    private var database: AppDatabase? = null

    fun init(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "literacy.db"
            ).build()
        }
    }

    fun get(context: Context): AppDatabase {
        if (database == null) {
            init(context)
        }
        return database!!
    }
}
