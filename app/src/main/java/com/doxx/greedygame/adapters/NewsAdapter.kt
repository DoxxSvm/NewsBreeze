package com.doxx.greedygame.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doxx.greedygame.R
import com.doxx.greedygame.models.Article
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter (
    private val onReadClicked: (Article) -> Unit,
    private val onSaveClicked: (Article) -> Unit
        ): RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(thumbnail)
            title.text = article.title
            desc.text = article.description
            date.text = article.publishedAt?.subSequence(0,10) ?: ""
            read.setOnClickListener {
                onReadClicked(article)
            }
            save.setOnClickListener {
                onSaveClicked(article)
            }
            item_save_icon.setOnClickListener {view->
                onSaveClicked(article)
                view.setBackgroundColor(Color.parseColor("#94C77D"))
            }
        }
    }


}


