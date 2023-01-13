package com.example.article.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey var codeArticle: String,
    var description: String,
    var family: String?,
    var pricenoIVA: Float,
    var stock: Float,
)

