package com.doxx.greedygame.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    var author: String?,
    val content: String?,
    var description: String?,
    val publishedAt: String?,
    var title: String?,
    var url: String?,
    val urlToImage: String?
) : Serializable