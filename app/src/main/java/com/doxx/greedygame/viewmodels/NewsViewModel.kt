package com.doxx.greedygame.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.doxx.greedygame.models.Article
import com.doxx.greedygame.models.NewsResponse
import com.doxx.greedygame.repository.NewsRepository
import com.doxx.greedygame.utils.Resource
import com.doxx.greedygame.utils.Sort
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage =1
    var breakingNewsResponse : NewsResponse? = null

    var searchNewsPage =1

    init {
        getBreakingNews("in")
    }
    private fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }
    fun searchNews(searchQuery : String) = viewModelScope.launch {
        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            response.body()?.let {
                breakingNewsResponse=it
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun getSavedNews()= newsRepository.getSavedNews()
    fun searchSavedNews(query: String)=newsRepository.searchNews(query)



}