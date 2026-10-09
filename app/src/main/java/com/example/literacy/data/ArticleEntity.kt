package com.example.literacy.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val level: Int,
    val content: String,
    val question: String,
    val answer: String
)
