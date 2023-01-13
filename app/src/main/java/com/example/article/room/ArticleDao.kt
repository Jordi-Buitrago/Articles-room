package com.example.article.room

import androidx.room.*

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article ORDER BY codeArticle ASC")
    suspend fun getArticles(): MutableList<Article>

    @Query("SELECT * FROM article WHERE codeArticle = :id")
    suspend fun getArticle(id: String): Article

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(friend: Article): Long

    @Delete
    suspend fun delete(friend: Article)

    @Update
    suspend fun update(friend: Article)

    @Query("DELETE FROM article")
    suspend fun deleteAll()
}