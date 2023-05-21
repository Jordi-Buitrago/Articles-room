package com.example.article.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 2)
abstract class MyDb : RoomDatabase() {
    abstract fun ArticleDao(): ArticleDao
}

