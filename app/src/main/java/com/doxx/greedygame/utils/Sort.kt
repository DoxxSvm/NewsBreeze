package com.doxx.greedygame.utils

import com.doxx.greedygame.models.Article

class Sort {
    companion object{
        fun sortByDate( articles : ArrayList<Article>):List<Article>{
            articles.sortBy {
                it.publishedAt
            }
            return articles
        }
    }
}