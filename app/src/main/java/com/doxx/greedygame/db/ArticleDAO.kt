package com.doxx.greedygame.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.doxx.greedygame.models.Article

@Dao
interface ArticleDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllArticles():LiveData<List<Article>>

    @Query ("SELECT * FROM articles WHERE title LIKE :query ORDER BY id DESC")
    fun searchNews(query :String): LiveData<List<Article>>


}