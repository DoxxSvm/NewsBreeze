package com.doxx.greedygame.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doxx.greedygame.R
import com.doxx.greedygame.adapters.NewsAdapter
import com.doxx.greedygame.db.ArticleDatabase
import com.doxx.greedygame.models.Article
import com.doxx.greedygame.repository.NewsRepository
import com.doxx.greedygame.utils.Resource
import com.doxx.greedygame.utils.Sort
import com.doxx.greedygame.viewmodels.NewsViewModel
import com.doxx.greedygame.viewmodels.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository= NewsRepository(ArticleDatabase(requireContext()),requireContext())
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        setUpRecyclerView()
        searchViewListener()
        sortClickListener()
        observe()
        saved_news.setOnClickListener {
            findNavController().navigate(R.id.action_breakingNewsFragment_to_savedNewsFragment)
        }
    }

    private fun observe() {
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e("Doxx", "Error: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        }
    }

    private fun sortClickListener() {
        filter.setOnClickListener {
            val popUpMenu = PopupMenu(requireContext(),it)
            popUpMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.publication->{
                        true
                    }
                    R.id.date->{
                        sortByDate()
                        true
                    }
                    else -> {false}
                }
            }
            popUpMenu.inflate(R.menu.sort_menu)
            popUpMenu.show()

        }
    }

    private fun sortByDate() {
        val sortedList = Sort.sortByDate(viewModel.breakingNewsResponse?.articles as ArrayList<Article>)
        newsAdapter.differ.submitList(sortedList)
        view?.let { Snackbar.make(it,"Sorted by date",Snackbar.LENGTH_SHORT).show() }
    }

    private fun searchViewListener() {
        var job: Job?=null
        search.addTextChangedListener {
            job?.cancel()
            job= MainScope().launch {
                delay(100L)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.breakingNewsResponse=null
                        viewModel.searchNews(it.toString())
                    }
                }
            }


        }
    }

    private fun hideProgressBar() {
        breakingNewsProgressBar.visibility=View.GONE
    }
    private fun showProgressBar() {
        breakingNewsProgressBar.visibility=View.VISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter= NewsAdapter(::onReadClicked,::onSaveClicked)
        articleRv.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    private fun onReadClicked(article: Article){
        var bundle = Bundle().apply {
            putSerializable("article",article)
        }
        findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
    }
    private fun onSaveClicked(article: Article){
        viewModel.saveArticle(article)
        view?.let { Snackbar.make(it,"Article saved successfully",Snackbar.LENGTH_SHORT).show() }
    }
}