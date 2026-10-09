package com.example.literacy.data.schema

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.literacy.data.LearningRecordEntity
import com.example.literacy.data.ArticleEntity

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecord(record: LearningRecordEntity)

    @Query("SELECT * FROM learning_records WHERE characterId = :characterId LIMIT 1")
    suspend fun getRecord(characterId: String): LearningRecordEntity?

    @Query("SELECT * FROM learning_records ORDER BY lastReviewAt DESC")
    suspend fun getAllRecords(): List<LearningRecordEntity>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    suspend fun getArticleById(id: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE level <= :maxLevel ORDER BY level ASC")
    suspend fun getAvailableArticles(maxLevel: Int): List<ArticleEntity>
}
