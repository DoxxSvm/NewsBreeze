package com.doxx.greedygame

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.doxx.greedygame.db.ArticleDatabase
import com.doxx.greedygame.repository.NewsRepository
import com.doxx.greedygame.viewmodels.NewsViewModel
import com.doxx.greedygame.viewmodels.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        window.statusBarColor = Color.BLACK
    }
}