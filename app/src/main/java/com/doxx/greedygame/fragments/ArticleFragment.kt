package com.doxx.greedygame.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.doxx.greedygame.R
import com.doxx.greedygame.db.ArticleDatabase
import com.doxx.greedygame.models.Article
import com.doxx.greedygame.repository.NewsRepository
import com.doxx.greedygame.viewmodels.NewsViewModel
import com.doxx.greedygame.viewmodels.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.item_news.view.*


class ArticleFragment : Fragment(R.layout.fragment_article) {
    val articleFragmentArgs : ArticleFragmentArgs by navArgs()
    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = articleFragmentArgs.article

        val newsRepository= NewsRepository(ArticleDatabase(requireContext()),requireContext())
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        initViews(article)
        listeners(article, view)
    }

    private fun initViews(article: Article) {
        Glide.with(this).load(article.urlToImage).into(img)
        date.text = article.publishedAt?.subSequence(0, 10) ?: ""
        title.text = article.title
        desc_article.text = article.content
        var authorName = article.author
        if (authorName == null) {
            authorName = "Author Name"
        }
        author.text = authorName
    }

    private fun listeners(article: Article, view: View) {
        save.setOnClickListener {
            viewModel.saveArticle(article)
            view.let {
                Snackbar.make(it, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
        save_icon.setOnClickListener {
            viewModel.saveArticle(article)
            view.let {
                Snackbar.make(it, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
        back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}