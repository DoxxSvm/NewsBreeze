package com.doxx.greedygame.api

import com.doxx.greedygame.models.NewsResponse
import com.doxx.greedygame.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        pageNumber: Int=1,
        @Query("sortBy")
        sortBy:String="popularity",
        @Query("apiKey")
        apiKey:String = API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int=1,
        @Query("searchIn")
        searchCategory:String = "title",
        @Query("apiKey")
        apiKey:String = API_KEY
    ) : Response<NewsResponse>
}