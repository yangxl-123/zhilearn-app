package com.example.literacy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_records")
data class LearningRecordEntity(
    @PrimaryKey val characterId: String,
    val status: String = "INITIAL",
    val lastReviewAt: Long = System.currentTimeMillis()
)
