package com.example.newsapptest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter(
    private val articles: List<Article>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val urlTextView: TextView = itemView.findViewById(R.id.urlTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)
        val publishedAtTextView: TextView = itemView.findViewById(R.id.publishedAtTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        init {
            itemView.setOnClickListener {
                onItemClick(articles[adapterPosition].url)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.sourceTextView.text = "Source: ${article.sourceName}"
        holder.authorTextView.text = "Author: ${article.author}"
        holder.titleTextView.text = article.title
        holder.descriptionTextView.text = "Description: ${article.description}"
        holder.urlTextView.text = "URL: ${article.url}"
        holder.publishedAtTextView.text = "Published At: ${article.publishedAt}"
        holder.contentTextView.text = "Content: ${article.content}"

        // Load image using Picasso library
        Picasso.get().load(article.urlToImage).placeholder(R.drawable.placeholder_image).into(holder.articleImageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}
