package com.doxx.greedygame.repository

import android.content.Context
import com.doxx.greedygame.api.RetrofitInstance
import com.doxx.greedygame.db.ArticleDatabase
import com.doxx.greedygame.models.Article

class NewsRepository(
    val db:ArticleDatabase,val context: Context
) {
    suspend fun getBreakingNews(countryCode:String, pageNumber: Int)=
        RetrofitInstance(context).Retrofit().retrofit.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int)=
        RetrofitInstance(context).Retrofit().retrofit.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)
    fun getSavedNews()=db.getArticleDao().getAllArticles()
    fun searchNews(query:String)=db.getArticleDao().searchNews(query)
}