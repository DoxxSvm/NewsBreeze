package com.doxx.greedygame.db

import androidx.room.TypeConverter
import com.doxx.greedygame.models.Article
import com.doxx.greedygame.models.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
    @TypeConverter
    fun fromArticle(article: Article):String{
        return Gson().toJson(article)
    }
    @TypeConverter
    fun fromString(value: String): Article {
        val listType = object :TypeToken<Article>(){}.type
        return Gson().fromJson(value,listType)
    }
//    @TypeConverter
//    fun fromSource(source: Source):String{
//        return Gson().toJson(source)
//    }
//    @TypeConverter
//    fun toSource(name: String): Source {
//        val listType = object :TypeToken<Source>(){}.type
//        return Gson().fromJson(name,listType)
//    }
}