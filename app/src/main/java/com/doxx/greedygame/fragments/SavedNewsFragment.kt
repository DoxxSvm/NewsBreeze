package com.doxx.greedygame.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.doxx.greedygame.R
import com.doxx.greedygame.adapters.SavedNewsAdapter
import com.doxx.greedygame.db.ArticleDatabase
import com.doxx.greedygame.models.Article
import com.doxx.greedygame.repository.NewsRepository
import com.doxx.greedygame.utils.Sort
import com.doxx.greedygame.viewmodels.NewsViewModel
import com.doxx.greedygame.viewmodels.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var savedNewsAdapter: SavedNewsAdapter
    var articleResponse:List<Article> = listOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsRepository= NewsRepository(ArticleDatabase(requireContext()),requireContext())
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        observe()
        setUpRecyclerView()
        searchViewListener()
        sortClickListener()
        clickListeners()
    }

    private fun clickListeners() {
        see_all.setOnClickListener {
            viewAllArticles()
        }
        viewAll.setOnClickListener {
            viewAllArticles()
        }
        back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                handleEmptyCase()
            }
            else{
                articleResponse = it
                savedNewsAdapter.differ.submitList(getFourArticles())
            }
        }
    }

    private fun handleEmptyCase() {
        view?.let { Snackbar.make(it,"Nothing in saved",Snackbar.LENGTH_SHORT).show() }
        root.visibility=View.GONE
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

    private fun sortByDate(){
        articleResponse = Sort.sortByDate(articleResponse as ArrayList)
        view?.let { Snackbar.make(it,"Sorted by Date",Snackbar.LENGTH_SHORT).show() }
    }
    private fun viewAllArticles() {
        savedNewsAdapter.differ.submitList(articleResponse)
        viewAll.visibility=View.GONE
        emptyView.visibility=View.VISIBLE
    }

    private fun getFourArticles():List<Article> {
        var tempList= arrayListOf<Article>()
        var size = min(4,articleResponse.size)
        for(i in 0..size-1){
            tempList.add(articleResponse.get(i))
        }
        return tempList
    }

    private fun setUpRecyclerView() {
        savedNewsAdapter= SavedNewsAdapter(::onReadClicked)
        saved_news_rv.apply {
            adapter=savedNewsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    private fun onReadClicked(article: Article){
        var bundle = Bundle().apply {
            putSerializable("article",article)
        }
        findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
    }
    private fun searchViewListener() {
        var job: Job?=null
        search.addTextChangedListener {
            job?.cancel()
            job= MainScope().launch {
                delay(100L)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.searchSavedNews(it.toString()).observe(viewLifecycleOwner,
                            Observer { articles->
                                Log.d("Doxx",articles.size.toString())
                                savedNewsAdapter.differ.submitList(articles)
                            })
                    }
                }
            }


        }
    }


}