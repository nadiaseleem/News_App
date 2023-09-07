package com.example.news.ui.home.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ItemArticleBinding
import com.example.news.ui.api.articlesModel.Article

class ArticlesAdapter(var articles: List<Article>? = null) :
    RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = articles?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles?.get(position)
        article?.let { article ->
            holder
                .binding.articleTitle.text = article.title
            holder
                .binding.articleDescription.text = article.description
            Glide.with(holder.itemView.context).load(article.urlToImage).centerCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(holder.binding.articleImg)

        }
    }

    fun updateArticles(articles: List<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }
}