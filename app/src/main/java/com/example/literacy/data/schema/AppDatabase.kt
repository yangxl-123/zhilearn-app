package com.example.literacy.data.schema

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.literacy.data.LearningRecordEntity
import com.example.literacy.data.ArticleEntity

@Database(
    entities = [LearningRecordEntity::class, ArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): Dao
}
